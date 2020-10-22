package inf3995.bixiapplication
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.vvalidator.form
import com.google.gson.Gson
import inf3995.bixiapplication.MainScreen.MainScreenActivity
import inf3995.test.bixiapplication.R
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
                val intent = Intent(this@SurveyActivity, MainScreenActivity::class.java)
                startActivity(intent)
            }
        }

        buttonSkip.setOnClickListener {
            val intent = Intent(this@SurveyActivity, MainScreenActivity::class.java)
            startActivity(intent)
        }
    }

    private fun sendSurveyData(surveyData: SurveyData) {

        // Post Server Ip address
        val retrofit5 = Retrofit.Builder()
            .baseUrl("https://${IpAddressDialog.ipAddressInput}")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().build())
            .build()

        var gson = Gson()
        var jsonString = gson.toJson(surveyData)

        val service5: WebBixiService = retrofit5.create(WebBixiService::class.java)
        val call5: Call<String> = service5.sendServerSurveyData(jsonString)

        call5.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG,"Réponse 2 du Serveur: ${response?.body()} 3:${response?.code()} 4:${response?.errorBody()} 5:${response?.message()}")
                Toast.makeText(this@SurveyActivity,"your answers were send successfully!", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG,"Erreur! 3: ${t.message}   4: ${t.cause}")
                Toast.makeText(this@SurveyActivity,"Error found while sending!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}