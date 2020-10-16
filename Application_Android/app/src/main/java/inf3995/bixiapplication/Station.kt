package inf3995.bixiapplication

import retrofit2.Call

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.Serializable



@kotlinx.serialization.Serializable
data class Station(
    var code :Int,
    var name: String,
    var latitude: Float,
    var longitude: Float,
    ): Serializable {}

object ServiceBuilder{
    private val client = okhttp3.OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://${IpAddressDialog.ipAddressInput}:${IpAddressDialog.portInput}/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val service: WebBixiService = retrofit.create(WebBixiService::class.java)
    val call: Call<String> = service.getAllStationCode()
}

data class data(
    var caracteristiqueTemps :Int,
    var valeur: Int
   ): Serializable {}

data class DataStation(
    var code :Int,
    var name: String,
    var annee: Int,
    var listData: ArrayList<data>
    ): Serializable {}

data class StatisticStation(
    var code :Int,
    var name: String,
    var annee: Int,
    var listData: ArrayList<data>,
    var graphique: String
): Serializable {}

data class PredictionStation(
    var code :Int,
    var name: String,
    var listData: ArrayList<data>,
    var graphique: String
): Serializable {}

data class GlobalDataStation(
    var annee: Int,
    var listData: ArrayList<data>,
): Serializable {}

data class GlobalStatisticStation(
    var annee: Int,
    var listData: ArrayList<data>,
    var graphique: String
): Serializable {}

data class GlobalPredictionStation(
    var listData: ArrayList<data>,
    var graphique: String
): Serializable {}