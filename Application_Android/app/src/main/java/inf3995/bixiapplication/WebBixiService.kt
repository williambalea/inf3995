package inf3995.bixiapplication

import inf3995.bixiapplication.Station
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.*

interface WebBixiService {

    @GET("/server/")
    fun getHelloWorld():Call<String>

  //  @POST("/")
   // fun getHelloWorld(@Body ipAddresss: String): Call<String>

    @GET("/station/code")
    fun getStationCode(@Field("body") code: Int):Call<Station>


    @GET("/engin1/data/usage/{year}/{time}/{station}")
    fun getStationStatistics(@Field("body") year:Int, time:String, station: Int):Call<String>

    @POST("/server/survey")
    fun sendServerSurveyData(@Body survey: String):Call<String>

    @GET("/engin1/station/all")
    fun getAllStationCode(): Call<String>
   // fun getAllStationCode(): Call<ArrayList<Station>>

}