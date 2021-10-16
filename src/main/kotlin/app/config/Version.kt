package app.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component

@Component
class Version(
  @Value("\${spring.application.version}")
  val applicationVersion: String
): HealthIndicator {

  override fun health(): Health =
    Health.up().withDetail("version", applicationVersion).build()

}