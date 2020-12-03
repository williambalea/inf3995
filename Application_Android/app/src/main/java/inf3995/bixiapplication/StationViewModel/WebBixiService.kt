package inf3995.bixiapplication.StationViewModel

import inf3995.bixiapplication.StationViewModel.StationLiveData.Station
import retrofit2.Call
import retrofit2.http.*

interface WebBixiService {

    @GET("/server/")
    fun getHelloWorld():Call<String>

    /********** Engine 1 methods *****************************/
    @GET("engine1/station/code")
    fun getStationCode(@Field("body") code: Int):Call<Station>

    @GET("/server/status/")
    fun getConnectivity():Call<String>

    @GET("/engine1/station/all")
    fun getAllStationCode(): Call<String>

    /********** Engine 2 methods *****************************/
    @GET("/engine2/data/usage/{year}/{time}/{station}")
    fun getStationStatistics(@Path("year") year:Int, @Path("time") time:String, @Path("station") station: Int): Call<String>

    @GET("/engine2/data/usage/{year}/{time}/all")
    fun getGlobalStatistics(@Path("year") year:Int, @Path("time") time:String): Call<String>

    @PUT("/server/survey")
    fun sendServerSurveyData(@Body survey: String):Call<String>

    /********** Engine 3 methods *****************************/
    @GET("/engine3/prediction/usage/{station}/{groupby}/{startDate}/{endDate}")
    fun getStationPrediction(@Path("station") station: Int, @Path("groupby") groupby:String,  @Path ("startDate") startDate: String, @Path ("endDate") endDate: String): Call<String>

    @GET("/engine3/prediction/usage/all/{groupby}/{startDate}/{endDate}")
    fun getGlobalPrediction(@Path("groupby") groupby:String,  @Path ("startDate") startDate: String, @Path ("endDate") endDate: String): Call<String>
    
    @GET("/engine3/prediction/error")
    fun getPredictionsErrors(): Call<String>

}