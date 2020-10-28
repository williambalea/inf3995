package inf3995.bixiapplication.Data

import java.io.Serializable



data class Station(
    var code :Int,
    var name: String,
    var latitude: Float,
    var longitude: Float,
    ): Serializable {}

data class data(
    var time :Array<String>,
    var departureValue: Array<Int>,
    var arrivalValue: Array<Int>
   ): Serializable {}


data class MonthlyStatisticStation(
    var data: data,
    var graph: String
): Serializable {}

data class DailyStatisticStation(

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