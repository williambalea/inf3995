package inf3995.bixiapplication

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface WebBixiService {

    @GET("Hello-World")
    fun getHelloWorld():Call<String>

    @FormUrlEncoded
    @POST("ip_server")
    fun sendServerIP(@Field("body") ipadress:String):Call<String>
}