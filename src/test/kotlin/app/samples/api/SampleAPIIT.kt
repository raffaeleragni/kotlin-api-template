package app.samples.api

import app.test.IntegrationTest
import app.testing.SAMPLE_TOKEN_WITH_ROLES
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.net.URI

@IntegrationTest
class SampleAPIIT {
  @LocalServerPort
  var randomServerPort: Int? = null

  @Test
  fun `check endpoint is authenticated`() {
    val restTemplate = RestTemplate();
    val uri = URI("http://localhost:$randomServerPort/sample");

    assertThrows<HttpClientErrorException.Unauthorized> { restTemplate.getForEntity(uri, String::class.java) }
  }

  @Test
  fun `check endpoint is returning 200 when token is used`() {
    val restTemplate = RestTemplate()
    val uri = URI("http://localhost:$randomServerPort/sample")
    val headers = HttpHeaders()
    headers["Authorization"] = "Bearer $SAMPLE_TOKEN_WITH_ROLES"
    assertThat(restTemplate.exchange( uri, HttpMethod.GET, HttpEntity<String>(headers), String::class.java).statusCode, `is`(HttpStatus.OK))
  }
}