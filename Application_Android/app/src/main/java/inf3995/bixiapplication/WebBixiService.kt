package inf3995.bixiapplication

import retrofit2.Call
import retrofit2.http.GET

interface WebBixiService {
    @GET("Test")
    fun getTest(): Call<String>
}