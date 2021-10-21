package app.config.security

import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.function.Function

@Component
class SecurityContextRepository(val manager: AuthenticationManager): ServerSecurityContextRepository {

  override fun save(swe: ServerWebExchange?, sc: SecurityContext?): Mono<Void?>? {
    throw UnsupportedOperationException("Not supported yet.")
  }

  override fun load(swe: ServerWebExchange): Mono<SecurityContext?>? {
    return Mono.justOrEmpty(swe.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
      .flatMap<SecurityContext?>(Function<String, Mono<out SecurityContext?>> { authHeader: String ->
        manager.authenticate(PreAuthenticatedAuthenticationToken("", authHeader))
          .map { authentication: Authentication? -> SecurityContextImpl(authentication) }
      })
  }
}
