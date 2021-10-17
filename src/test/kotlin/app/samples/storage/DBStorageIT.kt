package app.samples.storage

import app.test.IntegrationTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import java.util.*

@IntegrationTest
class DBStorageIT {
  @Autowired
  private lateinit var repository: DBStorage

  @Test
  fun `store and retrieve`() {
    val id = UUID.randomUUID().toString()
    val value = DBValue(id, "name")
    repository.save(value)
    assertThat(repository.findByIdOrNull( id), `is`(value))
  }
}
