package app.config.security

import io.jsonwebtoken.ExpiredJwtException
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import reactor.core.publisher.Mono

class AuthenticationManagerTest {

  private lateinit var manager: AuthenticationManager

  @BeforeEach
  fun setup() {
    manager = AuthenticationManager()
  }

  @Test
  fun `no token results empty`() {
    assertThat(manager.authenticate(null), `is`(Mono.empty<Authentication>()))
  }

  @ParameterizedTest
  @ValueSource(strings = ["", "asd", "{}"])
  fun `invalid tokens results empty`(token: String) {
    val auth = PreAuthenticatedAuthenticationToken("user", token)
    assertThat(manager.authenticate(auth), `is`(Mono.empty<Authentication>()))
  }

  @Test
  fun `valid token is not empty, but roles are`() {
    val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJyb2xlcyI6W119.yjh-DMdelyF78dO4LdVa--VDaJOcdk8OYJ-FOQnAkKA"
    val auth = PreAuthenticatedAuthenticationToken("user", token)
    assertThat(manager.authenticate(auth).block()?.authorities, `is`(emptyList<Roles>()))
  }

  @Test
  fun `token with date expired`() {
    val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjB9.JWKPB-5Q8rTYzl-MfhRGpP9WpDpQxC7JkIAGFMDZnpg"
    val auth = PreAuthenticatedAuthenticationToken("user", token)
    assertThrows<ExpiredJwtException> { manager.authenticate(auth) }
  }

  @Test
  fun `token with roles`() {
    val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJyb2xlcyI6WyJST0xFMSJdfQ.F0A_1bY5zItY27JTsdJz4t3PzRsI5ABrQlF_18G-rUo"
    val auth = PreAuthenticatedAuthenticationToken("user", token)
    val role = manager.authenticate(auth).block()?.authorities?.first()
    assertThat(role?.authority, `is`("ROLE1"))
  }

  @Test
  fun `token with wrong roles property type`() {
    val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJyb2xlcyI6MX0.-rYBWro5SLYwijt7M-lC74BXEOX_fD40xgLwHnoyhhA"
    val auth = PreAuthenticatedAuthenticationToken("user", token)
    var authorities = manager.authenticate(auth).block()?.authorities
    assertThat(authorities, `is`(emptyList()))
  }

  @Test
  fun `token with null roles`() {
    val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJyb2xlcyI6bnVsbH0.OfdetsikzlLqsj4K3Xzp0uaQk9ZTbvc9SGWAmbGf0yM"
    val auth = PreAuthenticatedAuthenticationToken("user", token)
    var authorities = manager.authenticate(auth).block()?.authorities
    assertThat(authorities, `is`(emptyList()))
  }
}
