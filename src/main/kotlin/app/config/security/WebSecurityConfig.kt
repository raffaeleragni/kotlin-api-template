package app.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig(val manager: AuthenticationManager, val repo: SecurityContextRepository) {

  @Bean
  fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
    return http.exceptionHandling()
      .authenticationEntryPoint {swe, _ -> Mono.fromRunnable { swe.response.statusCode = HttpStatus.UNAUTHORIZED }}
      .accessDeniedHandler {swe, _ -> Mono.fromRunnable { swe.response.statusCode = HttpStatus.FORBIDDEN }}
      .and()
      .csrf().disable()
      .formLogin().disable()
      .httpBasic().disable()
      .authenticationManager(manager)
      .securityContextRepository(repo)
      .authorizeExchange()
      .pathMatchers(HttpMethod.OPTIONS).permitAll()
      .pathMatchers("/swagger-ui/**").permitAll()
      .pathMatchers("/swagger-resources/**").permitAll()
      .pathMatchers("/v2/api-docs/**").permitAll()
      .pathMatchers("/health/**").permitAll()
      .pathMatchers("/prometheus/**").permitAll()
      .anyExchange().authenticated()
      .and()
      .build()
  }
}
