package app.samples.storage

import com.fasterxml.jackson.databind.ObjectMapper
import io.lettuce.core.RedisClient
import io.lettuce.core.codec.RedisCodec
import org.springframework.stereotype.Component
import java.nio.ByteBuffer
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
  private val sync = client.connect(Codec(mapper)).sync()

  operator fun get(key: String): RedisValue? {
    return sync[key]
  }

  operator fun set(key: String, value: RedisValue) {
    sync[key] = value
  }
}

private class Codec(val mapper: ObjectMapper): RedisCodec<String, RedisValue> {
  override fun decodeKey(bytes: ByteBuffer?): String {
    return bytes.toString()
  }

  override fun decodeValue(bytes: ByteBuffer?): RedisValue {
    return mapper.readValue<RedisValue>(bytes)
  }

  override fun encodeKey(key: String?): ByteBuffer {
    return ByteBuffer.wrap(key?.toByteArray())
  }

  override fun encodeValue(value: RedisValue?): ByteBuffer {
    return ByteBuffer.wrap(mapper.writeValueAsBytes(value))
  }
}

private inline fun <reified T> ObjectMapper.readValue(bytes: ByteBuffer?): T {
  val arr = bytes?.remaining()?.let { ByteArray(it) }
  bytes?.get(arr);
  return readValue(arr, T::class.java)
}
