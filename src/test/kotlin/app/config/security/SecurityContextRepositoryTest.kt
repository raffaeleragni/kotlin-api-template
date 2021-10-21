package app.config.security

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class SecurityContextRepositoryTest {
  lateinit var repo: SecurityContextRepository
  private val manager = mock<AuthenticationManager>() {
    on { authenticate(any()) } doReturn Mono.empty()
  }

  @BeforeEach
  fun setup() {
    repo = SecurityContextRepository(manager)
  }

  @Test
  fun `cannot save`() {
    assertThrows<UnsupportedOperationException> { repo.save(null, null) }
  }

  @Test
  fun `forward to authentication manager as is`() {
    val token = "xxxxaaaabbbb"
    var headers = HttpHeaders()
    headers[HttpHeaders.AUTHORIZATION] = "Bearer $token"
    val request = mock< ServerHttpRequest> {
      on { getHeaders() } doReturn headers
    }
    val swe = mock<ServerWebExchange> {
      on { getRequest() } doReturn request
    }

    repo.load(swe)?.block()

    verify(manager).authenticate(PreAuthenticatedAuthenticationToken("", "Bearer $token"))
  }

}