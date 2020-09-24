package inf3995.bixiapplication

import com.google.gson.annotations.SerializedName

data class GetData(
    @SerializedName ("origin") val ip:String,
    val url:String
) {
}