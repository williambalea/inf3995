package inf3995.bixiapplication

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.survey.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SurveyActivity : AppCompatActivity() {


    lateinit var surveyData: SurveyData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.survey)
        buttonSend.setOnClickListener {
            surveyData.email = editTextEmail.text.toString()
            surveyData.firstName = editTextFirstName.text.toString()
            surveyData.lastName = editTextLastName.text.toString()
            surveyData.age = Integer.parseInt(editTextAge.text.toString())
            surveyData.interest = checkBoxYesSurvey.isChecked

            sendSurveyData(surveyData)

        }

        buttonSkip.setOnClickListener {}

    }

    /*fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }*/

    private fun sendSurveyData(surveyData: SurveyData) {


        // Post Server Ip address
        val retrofit5 = Retrofit.Builder()
            .baseUrl("http://${IpAddressDialog.ipAddressInput}:${IpAddressDialog.portInput}/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service5: WebBixiService = retrofit5.create(WebBixiService::class.java)
        val call5: Call<SurveyData> = service5.sendServerSurveyData(surveyData)

        call5.enqueue(object: Callback<SurveyData> {
            override fun onResponse(call: Call<SurveyData>?, response: Response<SurveyData>?) {
                //Log.i(TAG,"Réponse 2 du Serveur: ${response?.body()}")
                Toast.makeText(this@SurveyActivity,"your answer were send successfully!", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<SurveyData>?, t: Throwable) {
                Toast.makeText(this@SurveyActivity,"Error found while sending!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}