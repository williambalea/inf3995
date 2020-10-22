package inf3995.bixiapplication
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
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
    val dialog = IpAddressDialog()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.survey)
        dialog.show(supportFragmentManager, null)

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
                if(!response?.body().isNullOrBlank())
                    Log.i(TAG,"Réponse 2 du Serveur: ${response?.body()}")
                else
                    Log.i(TAG,"${response?.body()} --->   code:${response?.code()}    message:${response?.message()}")
                val intent = Intent(this@SurveyActivity, MainScreenActivity::class.java)
                startActivity(intent)
            }
            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG,"Erreur when sending survey!    cause: ${t.cause}    message: ${t.message}")
                val builder = AlertDialog.Builder(this@SurveyActivity)
                builder.setTitle("Error while sending survey to server!").setMessage("cause: ${t.cause} \n message: ${t.message}")
                builder.show()
            }
        })
    }
}