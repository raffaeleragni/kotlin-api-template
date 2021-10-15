package app.config

import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class JacksonTest {
  @Test
  fun `test jackson date serialization`() {
    val mapper = JacksonConfig().objectMapper()

    val date = "\"2017-05-23T12:10:19Z\"";

    val datetime = mapper.readValue<ZonedDateTime>(date);
    val date2 = mapper.writeValueAsString(datetime);

    assertThat("Dates serialize", date, `is`(date2));
  }
}