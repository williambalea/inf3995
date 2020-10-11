package inf3995.test.bixiapplication

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET

interface WebBixiService {

    @GET("/")
    fun getHelloWorld(): Call<String>

    @GET("/station/code")
    fun getStationCode(@Field("body") code: Int):Call<Station>

    @GET("/station/all")
    fun getAllStationCode():Call<List<Station>>

}