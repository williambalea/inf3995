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

    @POST("/server/survey")
    @GET("/donnees/usage/<année>/<temps>/<station>")
    fun getStationStatistics(@Field("body") annee:Int, temps:String, code: Int):Call<String>

    fun sendServerSurveyData(@Body survey: String):Call<String>

    @GET("/engin1/station/all")
    fun getAllStationCode(): Call<String>
   // fun getAllStationCode(): Call<ArrayList<Station>>

}