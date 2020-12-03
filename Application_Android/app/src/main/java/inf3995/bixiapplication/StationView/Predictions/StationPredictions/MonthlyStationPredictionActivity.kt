package inf3995.bixiapplication.StationView.Predictions.StationPredictions

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import inf3995.bixiapplication.StationView.Dialog.IpAddressDialog
import inf3995.bixiapplication.StationView.Dialog.UnsafeOkHttpClient
import inf3995.bixiapplication.StationView.MainScreen.MainScreenActivity
import inf3995.bixiapplication.StationViewModel.StationLiveData.DataPredictionResponseStation
import inf3995.bixiapplication.StationViewModel.StationLiveData.Station
import inf3995.bixiapplication.StationViewModel.WebBixiService
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_code
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_name
import kotlinx.android.synthetic.main.activity_monthly_station_prediction.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class MonthlyStationPredictionActivity : AppCompatActivity() {
    var station : Station? = null
    lateinit var temps: String
    var code: Int = 0
    var year = 0
    var myImage: ImageView? = null
    var dateStart : String? = null
    var dateEnd : String? = null
    lateinit var table: TableLayout
    private val TAG = "Monthly Station Predictions values"
    var yearEnd = 2017
    var yearStart = 2017

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_station_prediction)
        val tempas = intent.getStringExtra("Temps")
        val annas = intent.getStringExtra("Annee")?.toInt()
        station = intent.getSerializableExtra("data") as Station

        table = findViewById(R.id.main_table)
        val dataStart = intent.getStringExtra("DateStart")
        val dataEnd = intent.getStringExtra("DateEnd")

        Station_code.text = station!!.code.toString()
        Station_name.text = station!!.name

        if (tempas != null) {
            temps = tempas
        }

        if (annas != null) {
            year = annas
        }

        if (dataStart != null) {
            dateStart = dataStart
        }

        if (dataEnd != null) {
            dateEnd = dataEnd
        }

        PredictTitle.text = getString(R.string.Monthly_Prediction_Title)
        predictionYearM.text = year.toString()
        code =  station!!.code
        myImage = findViewById(R.id.image)

        yearEnd = dateEnd!!.split('-')[2].toInt()
        yearStart = dateStart!!.split('-')[2].toInt()

        if(dateEnd != dateStart && ((yearEnd == yearStart)) ){
            requestToServer(IpAddressDialog.ipAddressInput)
        } else {
            val builder = AlertDialog.Builder(this@MonthlyStationPredictionActivity)
            builder.setTitle("Oups. Error in the dates entered !!!")
                .setMessage(" One of these conditions was not respected.\n"+
                        "- End date and Start date should be in the same year.\n" +
                        "- Start date should be prior to End date.\n")
            builder.setIcon(R.mipmap.ic_launcher)
            builder.show().setOnDismissListener {
                val intent = Intent(this, StationPredictionsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP;
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        MainScreenActivity.connectivity.observe(this, {
            if(it[0] == "DOWN" && it[2] == "UP") {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Engine Error!").setMessage("Connection with Engine 1 failed")
                builder.show().setOnDismissListener {
                    val intent = Intent(this, MainScreenActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
            }
            if ( it[0] == "UP" && it[2] == "DOWN") {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Engine Error!").setMessage("Connection with Engine 2 failed")
                builder.show().setOnDismissListener {
                    val intent = Intent(this, MainScreenActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
            }
            if ( it[0] == "DOWN" && it[2] == "DOWN") {
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
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().readTimeout(360, TimeUnit.SECONDS).build())
            .build()
        val service: WebBixiService = retrofit.create(WebBixiService::class.java)
        val call: Call<String> = service.getStationPrediction(code, temps, dateStart!!, dateEnd!!)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG, "Response Status  of monthly stationpredictions  from Server: ${response?.code()}")
                Log.i(TAG, "Response body of monthly station predictions from Server: ${response?.body()}")

                val arrayStationType = object : TypeToken<DataPredictionResponseStation>() {}.type
                val jObj: DataPredictionResponseStation = Gson().fromJson(response?.body(), arrayStationType)
                Log.i(TAG, "Object : $jObj")
                fillData(jObj)
                lllProgressBarM.visibility = View.GONE
            }

            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG, "Error when loading predictions!    cause:${t.cause}     message:${t.message}")
                val builder = AlertDialog.Builder(this@MonthlyStationPredictionActivity)
                builder.setTitle("Error while loading predictions!").setMessage("cause:${t.cause} \n message:${t.message}")
                builder.show()
            }
        })
    }

    private fun convertString64ToImage(base64String: String): Bitmap {
        val decodedString = Base64.decode(base64String, Base64.NO_WRAP)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    private fun fillData(jObj: DataPredictionResponseStation) {
        val myImageString = jObj.graph
        val image1 = findViewById<ImageView>(R.id.image)
        try {
            image1.setImageBitmap(convertString64ToImage(myImageString))
        } catch (e: Exception) {
            Log.e(TAG, "Error")
        }

        for (i in jObj.data.predictions.indices) {

            val time = jObj.data.time[i]
            val predictions = jObj.data.predictions[i]

            val tbRow = TableRow(this)
            val text0 = TextView(this)
            val text1 = TextView(this)
            val text2 = TextView(this)

            // Set the height and width of the TextView
            val scale = resources.displayMetrics.density
            val myHeight = (30 * scale + 0.5f).toInt()
            val myWidth = (132 *2* scale + 0.5f).toInt()

            // Set the first column of the table row
            text0.id = i + 1
            text0.setBackgroundColor(ContextCompat.getColor(this, R.color.colortablerow))
            text0.text = (i + 1).toString()
            text0.setTextColor(ContextCompat.getColor(this, R.color.colortextdata))
            text0.textSize = 18F
            text0.setTypeface(text0.typeface, Typeface.BOLD);
            text0.gravity = Gravity.CENTER_HORIZONTAL
            text0.apply {
                layoutParams = TableRow.LayoutParams(
                    myWidth,
                    myHeight,
                    3F
                )
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
            // add the column to the table row
            tbRow.addView(text0)

            // Set the second column of the table row
            text1.id = i + 2
            text1.setBackgroundColor(ContextCompat.getColor(this, R.color.colortablerow))
            text1.text = time
            text1.setTextColor(ContextCompat.getColor(this, R.color.colortextdata))
            text1.textSize = 18F
            text1.setTypeface(text1.getTypeface(), Typeface.BOLD);
            text1.gravity = Gravity.CENTER_HORIZONTAL
            text1.apply {
                layoutParams = TableRow.LayoutParams(
                    myWidth,
                    myHeight,
                    3F
                )
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
            tbRow.addView(text1)

            // Set the third column of the table row
            text2.id = i + 3
            text2.setBackgroundColor(ContextCompat.getColor(this, R.color.colortablerow))
            text2.text = predictions.toString()
            text2.setTextColor(ContextCompat.getColor(this, R.color.colortextdata))
            text2.textSize = 18F
            text2.setTypeface(text2.getTypeface(), Typeface.BOLD);
            text2.gravity = Gravity.CENTER_HORIZONTAL
            text2.apply {
                layoutParams = TableRow.LayoutParams(
                    myWidth,
                    myHeight,
                    3F
                )
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
            tbRow.addView(text2)

            // add the table row to the table Layout
            table.addView(tbRow)

        }

    }
}