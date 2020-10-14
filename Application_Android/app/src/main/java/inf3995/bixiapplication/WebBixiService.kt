package inf3995.bixiapplication

import inf3995.bixiapplication.Station
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface WebBixiService {

    @POST("/")
    fun getHelloWorld(@Body ipAddresss: String): Call<String>

    @GET("/station/code")
    fun getStationCode(@Field("body") code: Int):Call<Station>

    @GET("/station/all")
    fun getAllStationCode():Call<String>

}