package app.config

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.client.RestTemplate
import java.net.URI

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class HealthVersionIT {
  @LocalServerPort
  var randomServerPort: Int? = null
  @Autowired
  var objectMapper: ObjectMapper? = null

  @Test
  fun `health check is 200 and contains application version`() {
    val restTemplate = RestTemplate();
    val uri = URI("http://localhost:$randomServerPort/health/check");

    val response = restTemplate.getForEntity(uri, String::class.java);
    val json = objectMapper?.readValue(response.body, JsonNode::class.java)

    val version = JsonPath.read<String>(response.body, "\$.components.version.details.version");
    assertThat("Version returned is the one defined in test profile", version, `is`("test-version"));
  }
}