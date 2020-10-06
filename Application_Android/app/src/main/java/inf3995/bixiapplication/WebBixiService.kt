package inf3995.bixiapplication

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface WebBixiService {

    @GET("/")
    fun getHelloWorld():Call<String>

    @GET("/messages")
    fun getPostedMessages():Call<String>

    //@FormUrlEncoded
    @POST("ip_server")
    fun sendServerIP(/*@Field("body")*/ @Body ipAddress:String):Call<String>

    @POST("survey")
    fun sendServerSurveyData(@Body survey: String):Call<String>
}