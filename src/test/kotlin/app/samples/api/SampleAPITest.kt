package app.samples.api

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono

class SampleAPITest {
  @Test
  fun `response of sample api`() {
    val api = SampleAPI()
    assertThat(api.get().block(), `is`("hello"))
  }
}
