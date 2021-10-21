package app.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import java.util.function.Predicate.not

@Configuration
class SwaggerConfig {
  @Bean
  fun api(): Docket {
    return Docket(DocumentationType.SWAGGER_2)
      .select()
      .apis(RequestHandlerSelectors.any())
      .paths(not(PathSelectors.regex("/error.*")))
      .build()
      .securitySchemes(mutableListOf(ApiKey("Bearer", "Authorization", "header")) as List<SecurityScheme>?)
  }
}