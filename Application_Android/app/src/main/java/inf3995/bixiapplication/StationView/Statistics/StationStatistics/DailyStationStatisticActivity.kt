package inf3995.bixiapplication.StationView.Statistics.StationStatistics

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
import inf3995.bixiapplication.StationViewModel.StationLiveData.Station
import inf3995.bixiapplication.StationViewModel.WebBixiService
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_code
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_name
import kotlinx.android.synthetic.main.activity_daily_station_statistic.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class DailyStationStatisticActivity : AppCompatActivity() {

    var station : Station? = null
    lateinit var temps: String
    var code: Int = 0
    var annee= 0
    var myImage:ImageView? = null
    private val TAG = "Daily Station Statistics"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_station_statistic)
        val tempas = intent.getStringExtra("Temps")
        val annas = intent.getStringExtra("Annee")?.toInt()
        station = intent.getSerializableExtra("data") as Station

        Station_code.text = station!!.code.toString()
        Station_name.text = station!!.name

        if (tempas != null) {
            temps = tempas
        }
        if (annas != null) {
            annee = annas
        }
        statisticYear.text = annee.toString()
        code =  station!!.code
        myImage = findViewById(R.id.image)
        requestToServer(IpAddressDialog.ipAddressInput)
    }
    override fun onResume() {
        super.onResume()

        MainScreenActivity.connectivity.observe(this, Observer {

            if(it[0] == "DOWN" && it[1] == "UP") {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Engine Error!").setMessage("Connection with Engine 1 failed")
                builder.show().setOnDismissListener {
                    val intent = Intent(this, MainScreenActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
            }
            if ( it[0] == "UP" && it[1] == "DOWN") {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Engine Error!").setMessage("Connection with Engine 2 failed")
                builder.show().setOnDismissListener {
                    val intent = Intent(this, MainScreenActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
            }
            if ( it[0] == "DOWN" && it[1] == "DOWN") {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Engine Error!").setMessage("Connection with Engine 1 and Engine 2 failed")
                builder.show().setOnDismissListener {
                    val intent = Intent(this, MainScreenActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
            }
        })
    }

    private fun requestToServer(ipAddress: String?) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://$ipAddress/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().build())
            .build()
        val service: WebBixiService = retrofit.create(WebBixiService::class.java)
        val call: Call<String> = service.getStationStatistics(annee, temps, code)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG, "Response Status  of Daily Station statistics  from Server: ${response?.code()}")
                Log.i(TAG, "Response body of Daily Station statistics from Server: ${response?.body()}")

                val arrayStationType = object : TypeToken<DataResponseStation>() {}.type
                val jObj: DataResponseStation = Gson().fromJson(response?.body(), arrayStationType)
                Log.i(TAG, "Object : $jObj")
                fillData(jObj)
                lllProgressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG, "Error when loading statistics!    cause:${t.cause}     message:${t.message}")
                val builder = AlertDialog.Builder(this@DailyStationStatisticActivity)
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
        val image1 = findViewById<ImageView>(R.id.image)
        try{image1.setImageBitmap(convertString64ToImage(myImageString))}
        catch (e: Exception){
            Log.e(TAG,"error")
        }
        Log.i(TAG, "Display the graph ")

        text12.text = jObj.data.departureValue[0].toString()
        text13.text = jObj.data.arrivalValue[0].toString()
        text22.text = jObj.data.departureValue[1].toString()
        text23.text = jObj.data.arrivalValue[1].toString()
        text32.text = jObj.data.departureValue[2].toString()
        text33.text = jObj.data.arrivalValue[2].toString()
        text42.text = jObj.data.departureValue[3].toString()
        text43.text = jObj.data.arrivalValue[3].toString()
        text52.text = jObj.data.departureValue[4].toString()
        text53.text = jObj.data.arrivalValue[4].toString()
        text62.text = jObj.data.departureValue[5].toString()
        text63.text = jObj.data.arrivalValue[5].toString()
        text72.text = jObj.data.departureValue[6].toString()
        text73.text = jObj.data.arrivalValue[6].toString()

    }


}