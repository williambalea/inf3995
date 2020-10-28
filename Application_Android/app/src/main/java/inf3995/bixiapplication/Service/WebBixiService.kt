package inf3995.bixiapplication

import retrofit2.Call
import retrofit2.http.*

interface WebBixiService {

    @GET("/server/")
    fun getHelloWorld():Call<String>

    @GET("/engine1/station/all")
    fun getAllStationCode(): Call<String>

    @GET("engine1/station/code")
    fun getStationCode(@Field("body") code: Int):Call<Station>

    @GET("/engine2/data/usage/{year}/{time}/{station}")
    fun getStationStatistics(@Path("year") year:Int, @Path("time") time:String, @Path("station") station: Int): Call<String>

    @GET("/engine2/data/usage/{year}/{time}/all")
    fun getStationStatisticsGlobal(@Path("year") year:Int, @Path("time") time:String): Call<String>

    @POST("/server/survey")
    fun sendServerSurveyData(@Body survey: String):Call<String>

}