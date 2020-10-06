package inf3995.bixiapplication
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.vvalidator.form
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.survey.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class SurveyActivity : AppCompatActivity() {


    lateinit var surveyData: SurveyData
    var TAG :String = "SurveyActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.survey)

        form{
            input(editTextEmail){
                isEmail()
            }
            input(editTextFirstName){
                isNotEmpty()
                length().atLeast(3)
                length().lessThan(15)
            }
            input(editTextLastName){
                isNotEmpty()
                length().atLeast(3)
                length().lessThan(15)
            }
            input(editTextAge){
                isNotEmpty()
                isNumber().atLeast(1)
                isNumber().lessThan(130)
            }
            submitWith(buttonSend) { result ->
                surveyData = SurveyData(editTextEmail.text.toString(), editTextFirstName.text.toString(), editTextLastName.text.toString(), Integer.parseInt(editTextAge.text.toString()),checkBoxYesSurvey.isChecked)
                sendSurveyData(surveyData)
            }
        }

        buttonSkip.setOnClickListener {}
    }

    private fun sendSurveyData(surveyData: SurveyData) {

        // Post Server Ip address
        val retrofit5 = Retrofit.Builder()
            .baseUrl("http://${IpAddressDialog.ipAddressInput}:${IpAddressDialog.portInput}/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var gson = Gson()
        var jsonString = gson.toJson(surveyData)

        /*val surveyJsonObject = JsonObject()
        surveyJsonObject.addProperty("email", surveyData.email)
        surveyJsonObject.addProperty("firstName", surveyData.firstName)
        surveyJsonObject.addProperty("lastName", surveyData.lastName)
        surveyJsonObject.addProperty("age", surveyData.age)
        surveyJsonObject.addProperty("interested", surveyData.interest)*/

        println(jsonString)


        val service5: WebBixiService = retrofit5.create(WebBixiService::class.java)
        val call5: Call<String> = service5.sendServerSurveyData(jsonString)

        call5.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG,"Réponse 2 du Serveur: ${response?.body()}")
                Toast.makeText(this@SurveyActivity,"your answers were send successfully!", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG,"Erreur!")
                Toast.makeText(this@SurveyActivity,"Error found while sending!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}