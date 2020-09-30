package inf3995.bixiapplication

import com.google.gson.annotations.SerializedName

data class SurveyData(
    var email: String,
    var firstName: String,
    var lastName: String,
    var age : Int,
    var interest : Boolean,
) {
}