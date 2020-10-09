package inf3995.test.bixiapplication

import java.io.Serializable

data class Station(
    var code :Int,
    var name: String,
    var latitude: Float,
    var longitude: Float,

): Serializable {}