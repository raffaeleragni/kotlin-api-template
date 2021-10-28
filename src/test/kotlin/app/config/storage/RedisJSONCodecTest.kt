package app.config.storage

import app.config.JacksonConfig
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer

class RedisJSONCodecTest {
  private lateinit var mapper: ObjectMapper
  private lateinit var codec: RedisJSONCodec<String>

  @BeforeEach
  fun setup() {
    mapper = JacksonConfig().objectMapper()
    codec = RedisJSONCodec(mapper, String::class.java)
  }

  @Test
  fun `key serialization`() {
    val encoded = codec.encodeKey("key")
    assertThat(encoded, `is`(ByteBuffer.wrap("key".toByteArray())))
  }

  @Test
  fun `key deserialization`() {
    val buffer = ByteBuffer.wrap("key".toByteArray())
    val key = codec.decodeKey(buffer)
    assertThat(key, `is`("key"))
  }

  @Test
  fun `value serialization`() {
    val encoded = codec.encodeValue("value")
    assertThat(encoded, `is`(ByteBuffer.wrap("\"value\"".toByteArray())))
  }

  @Test
  fun `value deserialization`() {
    val buffer = ByteBuffer.wrap("\"value\"".toByteArray())
    val value = codec.decodeValue(buffer)
    assertThat(value, `is`("value"))
  }
}
