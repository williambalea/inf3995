package inf3995.bixiapplication.StationViewModel.StationLiveData

import java.io.Serializable



data class Station(
    var code :Int,
    var name: String,
    var latitude: Float,
    var longitude: Float,
    ): Serializable {}

data class Data(
    var time :Array<String>,
    var departureValue: Array<Int>,
    var arrivalValue: Array<Int>
   ): Serializable {}


data class DataResponseStation(
    var data: Data,
    var graph: String
): Serializable {}

