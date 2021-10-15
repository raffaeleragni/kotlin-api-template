package app.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component

@Component
class Version: HealthIndicator {
  @Value("\${spring.application.version}")
  var applicationVersion: String? = null

  override fun health(): Health =
    Health.up().withDetail("version", applicationVersion).build()

}