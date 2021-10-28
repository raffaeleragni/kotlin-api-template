package app.config

import app.test.IntegrationTest
import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.actuate.metrics.AutoConfigureMetrics
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.net.URI

@IntegrationTest
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

    val version = JsonPath.read<String>(response.body, "\$.components.version.details.version");
    assertThat("Version returned is the one defined in test profile", version, `is`("test-version"));

    val lb = URI("http://localhost:$randomServerPort/health/check/lb")
    assertThat(restTemplate.getForEntity(lb, String::class.java).statusCode, `is`(HttpStatus.OK))
  }

  @Test
  fun `security endpoints are disabled`() {
    val restTemplate = RestTemplate()
    var baseurl = "http://localhost:$randomServerPort"
    var uriheap = URI("$baseurl/heapdump")
    var urienv = URI("$baseurl/env")
    var prometheus = URI("$baseurl/prometheus")

    assertThrows<HttpClientErrorException.Unauthorized>{ restTemplate.getForEntity(uriheap, String::class.java) }
    assertThrows<HttpClientErrorException.Unauthorized>{ restTemplate.getForEntity(urienv, String::class.java) }

    assertThat(restTemplate.getForEntity(prometheus, String::class.java).statusCode, `is`(HttpStatus.OK))
  }
}
