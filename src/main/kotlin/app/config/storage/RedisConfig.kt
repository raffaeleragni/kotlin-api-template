package app.config.storage

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.resource.ClientResources
import io.lettuce.core.resource.DefaultClientResources
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(RedisProperties::class)
class RedisConfig(
  @Value("\${spring.redis.host}")
  val host: String,
  @Value("\${spring.redis.port}")
  val port: Int
) {

  @Bean
  fun clientResources(): ClientResources {
    return DefaultClientResources.create();
  }

  @Bean
  fun redisClient(clientResources: ClientResources): RedisClient {
    return RedisClient.create(clientResources, RedisURI.create(host, port));
  }
}
