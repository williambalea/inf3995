package inf3995.bixiapplication

import java.io.Serializable

data class Station(
    var code :Int,
    var name: String,
    var latitude: Float,
    var longitude: Float,
    ): Serializable {}

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