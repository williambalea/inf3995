package inf3995.bixiapplication.StationStatistics

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import inf3995.bixiapplication.*
import inf3995.bixiapplication.Data.MonthlyStatisticStation
import inf3995.bixiapplication.Dialog.IpAddressDialog
import inf3995.bixiapplication.Service.WebBixiService
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.*
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.imageGlobal
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.textGlobal12
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.textGlobal13
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.textGlobal22
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.textGlobal23
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.textGlobal32
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.textGlobal33
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.textGlobal42
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.textGlobal43
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.textGlobal52
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.textGlobal53
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.textGlobal62
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.textGlobal63
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.textGlobal72
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.textGlobal73
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class DailyStationStatisticGlobalActivity : AppCompatActivity() {

    lateinit var temps: String
    var annee= 0
    var myImage:ImageView? = null
    private val TAG = "Monthly Station Statistics"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_station_statistic_global)
        val tempas = intent.getStringExtra("timeGlobal")
        val annas = intent.getStringExtra("yearGlobal")?.toInt()

        if (tempas != null) {
            temps = tempas
        }
        if (annas != null) {
            annee = annas
        }
        statisticGlobalYear.text = annee.toString()
        myImage = findViewById(R.id.image)
        requestToServer(IpAddressDialog.ipAddressInput)

    }


    private fun requestToServer(ipAddress: String?) {

        // get check connexion with Server Hello from Server
        val retrofit = Retrofit.Builder()
            .baseUrl("https://$ipAddress/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().build())
            .build()
        val service: WebBixiService = retrofit.create(WebBixiService::class.java)
        val call: Call<String> = service.getStationStatisticsGlobal(annee, temps)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG, "Réponse des Statistiques du Serveur: ${response?.body()}")
                Log.i(TAG, "Status de reponse  des Statistiques du Serveur: ${response?.code()}")

                val arrayStationType = object : TypeToken<MonthlyStatisticStation>() {}.type
                val jObj: MonthlyStatisticStation = Gson().fromJson(response?.body(), arrayStationType)
                Log.i(TAG, "L'objet : $jObj")
                fillData(jObj)
                lllProgressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG, "Error when receiving statistic!    cause:${t.cause}     message:${t.message}")
                val builder = AlertDialog.Builder(this@DailyStationStatisticGlobalActivity)
                builder.setTitle("Error while loading statistic!").setMessage("cause:${t.cause} \n message:${t.message}")
                builder.show()
            }
        })
    }

    private fun convertString64ToImage(base64String: String): Bitmap {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    private fun fillData(jObj: MonthlyStatisticStation) {
        val myImageString = jObj.graph
        imageGlobal.setImageBitmap(convertString64ToImage(myImageString))
        Log.i(TAG, "affichage du graphique ")

        textGlobal12.setText(jObj.data.departureValue[0].toString())
        textGlobal13.setText(jObj.data.arrivalValue[0].toString())
        textGlobal22.setText(jObj.data.departureValue[1].toString())
        textGlobal23.setText(jObj.data.arrivalValue[1].toString())
        textGlobal32.setText(jObj.data.departureValue[2].toString())
        textGlobal33.setText(jObj.data.arrivalValue[2].toString())
        textGlobal42.setText(jObj.data.departureValue[3].toString())
        textGlobal43.setText(jObj.data.arrivalValue[3].toString())
        textGlobal52.setText(jObj.data.departureValue[4].toString())
        textGlobal53.setText(jObj.data.arrivalValue[4].toString())
        textGlobal62.setText(jObj.data.departureValue[5].toString())
        textGlobal63.setText(jObj.data.arrivalValue[5].toString())
        textGlobal72.setText(jObj.data.departureValue[6].toString())
        textGlobal73.setText(jObj.data.arrivalValue[6].toString())

    }

}