package app.config.storage

import com.fasterxml.jackson.databind.ObjectMapper
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.resource.ClientResources
import io.lettuce.core.resource.DefaultClientResources
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

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

class RedisJSONCodec<T>(
  private val mapper: ObjectMapper,
  private val klass: Class<T>
): RedisCodec<String, T> {

  override fun decodeKey(bytes: ByteBuffer?): String {
    return bytes.string()
  }

  override fun decodeValue(bytes: ByteBuffer?): T {
    return mapper.readValue(bytes.string(), klass)
  }

  override fun encodeKey(key: String?): ByteBuffer {
    return key.bytes()
  }

  override fun encodeValue(value: T): ByteBuffer {
    return mapper.writeValueAsString(value).bytes()
  }
}

private fun ByteBuffer?.string() = StandardCharsets.UTF_8.decode(this).toString()
private fun String?.bytes() = StandardCharsets.UTF_8.encode(this)
