package inf3995.bixiapplication

import inf3995.bixiapplication.station.Station
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
    @POST("station")
    fun sendStation(@Field("body") surver: Station):Call<Station>

}