package app.samples.client

import retrofit2.Call
import retrofit2.http.GET

interface APIClient {
  @GET("/")
  fun call(): Call<String>
}
