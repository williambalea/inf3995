package inf3995.bixiapplication.station

data class Station (var code:Int,
                    var name:String,
                    var latitude: Float,
                    var longitude:Float
)   {}
data class RawData (var temps:Int,
            var StationDepartures: Int){}
data class ReqData(var annee:Int, var temps: String, var name:String){}
data class PredData (var temps:Int, var difference: Float){}

data class StatOfStation(var donnees:List<RawData>,var graphique:String){}
data class PredOfStation(var donnees:List<PredData>,var graphique:String){}
