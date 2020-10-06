package inf3995.bixiapplication

import inf3995.bixiapplication.stationList.PredData
import inf3995.bixiapplication.stationList.ReqData
import inf3995.bixiapplication.stationList.StatOfStation
import inf3995.bixiapplication.stationList.Station
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
    @POST("/station/recherche")
    fun getSearchedStation(@Field("body") name: String):Call<List<Station>>

    @GET("/station/code")
    fun getStationCode(@Field("body") code: Int):Call<Station>

    @GET("/station/all")
    fun getAllStationCode(@Field("body") code: Int):Call<List<Station>>

    @GET("/donnees/usage/annee/temps/station")
    fun getStationDonnees(@Field("body") reqData: ReqData):Call<StatOfStation>

    @GET("/prediction/usage/station")
    fun getStationPrediction(@Field("body") reqData: ReqData):Call<StatOfStation>

    @GET("/prediction/erreur")
    fun getStationErrorPrediction(@Field("body") predData: PredData):Call<StatOfStation>

}