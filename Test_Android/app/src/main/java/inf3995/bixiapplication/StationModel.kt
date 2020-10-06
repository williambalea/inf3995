package inf3995.bixiapplication

import java.io.Serializable


data class Station (var code:Int,
                    var name:String,
                    var latitude: Float,
                    var longitude:Float
){}

data class RawData (var time:Int,
                    var stationDepartures: Int){}

data class ReqData(var year:Int, var time: String, var name:String){}

data class PredData (var time:Int, var difference: Float){}

data class StatOfStation(var data:List<RawData>,var graphic:String){}

data class PredOfStation(var data:List<PredData>,var graphic:String){}
