package inf3995.bixiapplication

import retrofit2.Call
import retrofit2.http.GET


interface HttpWebServicesString {
   @GET("user-agent")
    fun getUserAgent(): Call<String>

    @GET("ip")
    fun getUserIP():Call<String>
}