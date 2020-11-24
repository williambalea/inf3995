package inf3995.bixiapplication.StationView.Statistics.GlobalStatistics

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import inf3995.bixiapplication.StationView.Dialog.IpAddressDialog
import inf3995.bixiapplication.StationView.Dialog.UnsafeOkHttpClient
import inf3995.bixiapplication.StationView.MainScreen.MainScreenActivity
import inf3995.bixiapplication.StationViewModel.StationLiveData.DataResponseStation
import inf3995.bixiapplication.StationViewModel.WebBixiService
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_hourly_station_statistic_global.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class HourlyStationStatisticGlobalActivity : AppCompatActivity() {
    lateinit var time: String
    var year= 0
    var myImage: ImageView? = null
    private val TAG = "Hourly Station Statistics Global"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hourly_station_statistic_global)
        val tempas = intent.getStringExtra("timeGlobal")
        val annas = intent.getStringExtra("yearGlobal")?.toInt()

        if (tempas != null) {
            time = tempas
        }

        if (annas != null) {
            year = annas
        }
        statisticYearGlobal.text = year.toString()
        myImage = findViewById(R.id.imageGlobal)
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
        val call: Call<String> = service.getGlobalStatistics(year, time)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG, "Réponse des Statistiques du Serveur: ${response?.body()}")
                Log.i(TAG, "Status de reponse  des Statistiques du Serveur: ${response?.code()}")
                Log.i(
                    TAG,
                    "Message de reponse  des Statistiques du Serveur: ${response?.message()}"
                )

                val arrayStationType = object : TypeToken<DataResponseStation>() {}.type
                val jObj: DataResponseStation = Gson().fromJson(response?.body(), arrayStationType)
                Log.i(TAG, "L'objet : $jObj")
                fillData(jObj)
                lllProgressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG, "Error when receiving statistic!    cause:${t.cause}     message:${t.message}")
                val builder = AlertDialog.Builder(this@HourlyStationStatisticGlobalActivity)
                builder.setTitle("Error while loading statistic!").setMessage("cause:${t.cause} \n message:${t.message}")
                builder.show()
            }
        })
    }

    override fun onResume() {
        super.onResume()

        MainScreenActivity.listen.observe(this, Observer {

            if(it[1] == "DOWN"){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Engine Error!").setMessage("There may be a problem with Engine 2")
                builder.show().setOnDismissListener {
                    val intent = Intent(this, MainScreenActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP;
                    startActivity(intent)
                }
            }

        })

    }

    private fun convertString64ToImage(base64String: String): Bitmap {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    private fun fillData(jObj: DataResponseStation) {
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
        textGlobal82.setText(jObj.data.departureValue[7].toString())
        textGlobal83.setText(jObj.data.arrivalValue[7].toString())
        textGlobal92.setText(jObj.data.departureValue[8].toString())
        textGlobal93.setText(jObj.data.arrivalValue[8].toString())
        textGlobal102.setText(jObj.data.departureValue[9].toString())
        textGlobal103.setText(jObj.data.arrivalValue[9].toString())
        textGlobal112.setText(jObj.data.departureValue[10].toString())
        textGlobal113.setText(jObj.data.arrivalValue[10].toString())
        textGlobal122.setText(jObj.data.departureValue[11].toString())
        textGlobal123.setText(jObj.data.arrivalValue[11].toString())
        textGlobal132.setText(jObj.data.departureValue[12].toString())
        textGlobal133.setText(jObj.data.arrivalValue[12].toString())
        textGlobal142.setText(jObj.data.departureValue[13].toString())
        textGlobal143.setText(jObj.data.arrivalValue[13].toString())
        textGlobal152.setText(jObj.data.departureValue[14].toString())
        textGlobal153.setText(jObj.data.arrivalValue[14].toString())
        textGlobal162.setText(jObj.data.departureValue[15].toString())
        textGlobal163.setText(jObj.data.arrivalValue[15].toString())
        textGlobal172.setText(jObj.data.departureValue[16].toString())
        textGlobal173.setText(jObj.data.arrivalValue[16].toString())
        textGlobal182.setText(jObj.data.departureValue[17].toString())
        textGlobal183.setText(jObj.data.arrivalValue[17].toString())
        textGlobal192.setText(jObj.data.departureValue[18].toString())
        textGlobal193.setText(jObj.data.arrivalValue[18].toString())
        textGlobal202.setText(jObj.data.departureValue[19].toString())
        textGlobal203.setText(jObj.data.arrivalValue[19].toString())
        textGlobal212.setText(jObj.data.departureValue[20].toString())
        textGlobal213.setText(jObj.data.arrivalValue[20].toString())
        textGlobal222.setText(jObj.data.departureValue[21].toString())
        textGlobal223.setText(jObj.data.arrivalValue[21].toString())
        textGlobal232.setText(jObj.data.departureValue[22].toString())
        textGlobal233.setText(jObj.data.arrivalValue[22].toString())
        textGlobal242.setText(jObj.data.departureValue[23].toString())
        textGlobal243.setText(jObj.data.arrivalValue[23].toString())

    }

}