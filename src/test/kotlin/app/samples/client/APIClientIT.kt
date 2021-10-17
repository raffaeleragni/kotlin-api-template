package app.samples.client

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class APIClientIT {
  @Test
  fun `check simple request`() {
    val server = MockWebServer()
    server.enqueue(MockResponse().setBody("{}"))
    val client = retrofitMake<APIClient>(server)

    val result = client.call().execute().body()!!

    assertThat(result, `is`("{}"))

    val request = server.takeRequest()
    assertThat(request.requestLine, `is`("GET / HTTP/1.1"));
  }
}

private inline fun <reified T> retrofitMake(server: MockWebServer): T {
  server.start()
  val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(server.url("/").toString())
    .build()

  return retrofit.create(T::class.java)
}
