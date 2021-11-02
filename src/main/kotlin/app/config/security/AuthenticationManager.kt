package app.config.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationManager: ReactiveAuthenticationManager {
  override fun authenticate(authentication: Authentication?): Mono<Authentication> {
    val authentication = authentication?: return Mono.empty()
    val header = authentication.credentials.toString()
    if (!isValidHttpHeader(header))
      return Mono.empty()

    // This implementation assumes to be behind a gateway that checks the signature already
    var tokenWithoutSig = extractJWTHeaderAndBodyFromHttpHeader(header)
    val token = parse(tokenWithoutSig)
    val result = PreAuthenticatedAuthenticationToken(authentication.principal, token, rolesFromToken(token))
    return Mono.just(result)
  }
}

private fun isValidHttpHeader(header: String): Boolean = header.startsWith("Bearer ")

private fun extractJWTHeaderAndBodyFromHttpHeader(header: String) : String =
  header.substring("Bearer ".length, header.lastIndexOf(".") + 1)

private fun parse(token: String): Jwt<Header<*>, Claims> =
  Jwts.parserBuilder().build().parseClaimsJwt(token)

private fun rolesFromToken(token: Jwt<Header<*>, Claims>): List<GrantedAuthority> =
  ((token.body["roles"] as List<String>).map {Roles.valueOf(it)} )
