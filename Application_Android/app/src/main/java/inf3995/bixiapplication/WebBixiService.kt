package inf3995.bixiapplication

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface WebBixiService {

    @GET("/")
    fun getHelloWorld():Call<String>

    @GET("/messages")
    fun getPostedMessages():Call<String>

    @FormUrlEncoded
    @POST("ip_server")
    fun sendServerIP(@Field("body") ipAddress:String):Call<String>

    @FormUrlEncoded
    @POST("survey_data ")
    fun sendServerSurveyData(@Field("body") survey:SurveyData):Call<SurveyData>
}