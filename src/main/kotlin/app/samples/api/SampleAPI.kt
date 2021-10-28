package app.samples.api

import app.config.Affinity
import app.config.DeploymentAffinity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@DeploymentAffinity(Affinity.API)
@RequestMapping("/sample")
class SampleAPI {
  @GetMapping("/")
  fun get(): Mono<String> {
    return Mono.just("hello")
  }
}