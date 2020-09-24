package inf3995.bixiapplication

import retrofit2.Call
import retrofit2.http.GET

interface HttpWebServicesJson {
    @GET("get")
    fun getUserInfo(): Call<GetData>
}