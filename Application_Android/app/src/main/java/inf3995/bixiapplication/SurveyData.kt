package inf3995.bixiapplication

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class SurveyData(
    var email: String,
    var firstName: String,
    var lastName: String,
    var age : Int,
    var interest : Boolean,
) {
}

data class SurveyData1(var surveyData1: JSONObject){
    val surveyData = JSONObject("""{"email":"","firstName":"","lastName":"", "age":"", "interest:""}""")
}