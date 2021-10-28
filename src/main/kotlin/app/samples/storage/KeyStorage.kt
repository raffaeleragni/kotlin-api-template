package app.samples.storage

import app.config.storage.RedisJSONCodec
import com.fasterxml.jackson.databind.ObjectMapper
import io.lettuce.core.RedisClient
import org.springframework.stereotype.Component
import java.time.Instant

data class RedisValue(
  val id: Long,
  val name: String,
  val date: Instant
)

@Component
class KeyStorage(
  client: RedisClient,
  mapper: ObjectMapper
) {
  private val sync = client
    .connect(RedisJSONCodec(mapper, RedisValue::class.java))
    .sync()

  operator fun get(key: String): RedisValue? {
    return sync[key]
  }

  operator fun set(key: String, value: RedisValue) {
    sync[key] = value
  }
}
