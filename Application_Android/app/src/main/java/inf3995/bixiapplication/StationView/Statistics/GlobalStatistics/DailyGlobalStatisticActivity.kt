package inf3995.bixiapplication.StationView.Statistics.GlobalStatistics

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
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
import kotlinx.android.synthetic.main.activity_daily_station_statistic_global.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class DailyGlobalStatisticActivity : AppCompatActivity() {

    lateinit var temps: String
    var annee= 0
    var myImage:ImageView? = null
    private val TAG = "Daily Global Statistics"

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
    private fun requestToServer(ipAddress: String?) {

        // get check connexion with Server Hello from Server
        val retrofit = Retrofit.Builder()
            .baseUrl("https://$ipAddress/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().readTimeout(120, TimeUnit.SECONDS).build())
            .build()
        val service: WebBixiService = retrofit.create(WebBixiService::class.java)
        val call: Call<String> = service.getGlobalStatistics(annee, temps)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG, "Response Status  of Global statistics  from Server: ${response?.code()}")
                Log.i(TAG, "Response body of Global statistics from Server: ${response?.body()}")

                val arrayStationType = object : TypeToken<DataResponseStation>() {}.type
                val jObj: DataResponseStation = Gson().fromJson(response?.body(), arrayStationType)
                Log.i(TAG, "L'objet : $jObj")
                fillData(jObj)
                lllProgressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG, "Error when loading statistics!    cause:${t.cause}     message:${t.message}")
                val builder = AlertDialog.Builder(this@DailyGlobalStatisticActivity)
                builder.setTitle("Error while loading statistics!").setMessage("cause:${t.cause} \n message:${t.message}")
                builder.show()
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
        Log.i(TAG, "Display the graph")

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