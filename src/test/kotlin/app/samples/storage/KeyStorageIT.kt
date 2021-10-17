package app.samples.storage

import app.test.IntegrationTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant

@IntegrationTest
class KeyStorageIT {
  @Autowired
  lateinit var storage: KeyStorage

  @Test
  fun `store and retrieve`() {
    val value = RedisValue(1L, "asd", Instant.now())
    storage["test"] = value
    assertThat(storage["test"], `is`(value))
  }
}
