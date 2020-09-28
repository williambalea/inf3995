package inf3995.bixiapplication.station

data class Station (var id:Long,
                    var code:String,
                    var nom:String,
                    var latitude: Float,
                    var longitude:Float
)   {
    constructor(code:String, nom:String, latitude:Float, longitude:Float): this(-1,code, nom, latitude, longitude)
}