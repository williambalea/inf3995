package inf3995.bixiapplication
import retrofit2.Call
import retrofit2.http.*

interface WebBixiService {

    @GET("/server")
    fun getHelloWorld():Call<String>

    @GET("/server/messages")
    fun getPostedMessages():Call<String>

    @POST("/server/ip_server")
    fun sendServerIP(@Body ipAddress:String):Call<String>

    @POST("/server/survey")
    fun sendServerSurveyData(@Body survey: String):Call<String>
}