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
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import inf3995.bixiapplication.StationView.Dialog.IpAddressDialog
import inf3995.bixiapplication.StationView.Dialog.UnsafeOkHttpClient
import inf3995.bixiapplication.StationView.MainScreen.MainScreenActivity
import inf3995.bixiapplication.StationViewModel.StationLiveData.DataPredictionResponseStation
import inf3995.bixiapplication.StationViewModel.StationLiveData.Station
import inf3995.bixiapplication.StationViewModel.WebBixiService
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_hourly_station_prediction.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


class HourlyStationPredictionActivity : AppCompatActivity() {

    var station : Station? = null
    lateinit var temps: String
    var indicator: String? = null
    var code: Int = 0
    var annee= 0
    var myImage: ImageView? = null
    var dateStart : String? = null
    var dateEnd : String? = null
    private val TAG = "Hourly Station Prediction"
    lateinit var table: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hourly_station_prediction)
        station = intent.getSerializableExtra("data") as Station
        val tempas = intent.getStringExtra("Temps")
        val annas = intent.getStringExtra("Annee")?.toInt()
        val dataStart = intent.getStringExtra("DateStart")
        val dataEnd = intent.getStringExtra("DateEnd")
        table = findViewById(R.id.main_table)
        Station_codeH.text = station!!.code.toString()
        Station_nameH.text = station!!.name


        if (tempas != null) {
            temps = tempas
        }

        if (annas != null) {
            annee = annas
        }
        if (dataStart != null) {
            dateStart = dataStart
        }

        if (dataEnd != null) {
            dateEnd = dataEnd
        }

        PredictTitleH.text = getString(R.string.Hourly_Prediction_Title)
        PredictionYearH.text = annee.toString()
        code =  station!!.code
        myImage = findViewById(R.id.image)
        requestToServer(IpAddressDialog.ipAddressInput)
    }
    override fun onResume() {
        super.onResume()

        MainScreenActivity.listen.observe(this, Observer {

            if(it[2] == "DOWN"){
                Toast.makeText(
                    this,
                    "Engine Problem!",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(this, MainScreenActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }

        })

    }
    private fun requestToServer(ipAddress: String?) {

        // get check connexion with Server Hello from Server
        val retrofit = Retrofit.Builder()
            .baseUrl("https://$ipAddress/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().build())
            .build()
        val service: WebBixiService = retrofit.create(WebBixiService::class.java)
        val call: Call<String> = service.getStationPrediction(code, temps, dateStart!!, dateEnd!!)
        //val call: Call<String> = service.getStationStatistics(annee, temps, code)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG, "Réponse des predictions du Serveur: ${response?.body()}")
                Log.i(TAG, "Status de reponse  des predictions du Serveur: ${response?.code()}")
                Log.i(TAG,"Message de reponse  des predictions du Serveur: ${response?.message()}")

                val arrayStationType = object : TypeToken<DataPredictionResponseStation>() {}.type
                val jObj: DataPredictionResponseStation = Gson().fromJson(response?.body(), arrayStationType)
                Log.i(TAG, "L'objet : $jObj")
                fillData(jObj)
                lllProgressBarH.visibility = View.GONE
            }

            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG, "Error when receiving predictions!    cause:${t.cause}     message:${t.message}")
                val builder = AlertDialog.Builder(this@HourlyStationPredictionActivity)
                builder.setTitle("Error while loading predictions!").setMessage("cause:${t.cause} \n message:${t.message}")
                builder.show()
            }
        })
    }

    private fun convertString64ToImage(base64String: String): Bitmap {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    private fun fillData(jObj: DataPredictionResponseStation) {
        val myImageString = jObj.graph
        val image1 = findViewById(R.id.image) as ImageView
        try {
            image1.setImageBitmap(convertString64ToImage(myImageString))
        } catch (e: Exception) {
            Log.e(TAG, "error")
        }
        //image1.setImageBitmap(convertString64ToImage(myImageString))
        Log.i(TAG, "affichage du graphique ")

        for (i in 0 until jObj.data.predictions.size) {

            val time = jObj.data.time[i]
            val predictions = jObj.data.predictions[i]

            val tbrow = TableRow(this)
            val text0 = TextView(this)
            val text1 = TextView(this)
            val text2 = TextView(this)


            // Set the heigth and width of the TextView
            val scale = resources.displayMetrics.density
            val myheight = (30 * scale + 0.5f).toInt()
            val mywidth = (132 *2* scale + 0.5f).toInt()
            // Set the first column of the table row

            text0.setId(i + 1)
            text0.setBackgroundColor(ContextCompat.getColor(this, R.color.colortablerow))
            text0.setText((i + 1).toString())
            text0.setTextColor(ContextCompat.getColor(this, R.color.colortextdata))
            text0.setTextSize(18F)
            text0.setTypeface(text0.getTypeface(), Typeface.BOLD);
            text0.gravity = Gravity.CENTER_HORIZONTAL
            text0.apply {
                layoutParams = TableRow.LayoutParams(
                    mywidth,
                    myheight
                )
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
            // add the column to the tablerow
            tbrow.addView(text0)

            // Set the second column of the table row
            text1.setId(i + 2)
            text1.setBackgroundColor(ContextCompat.getColor(this, R.color.colortablerow))
            text1.setText(time)
            text1.setTextColor(ContextCompat.getColor(this, R.color.colortextdata))
            text1.setTextSize(18F)
            text1.setTypeface(text1.getTypeface(), Typeface.BOLD);
            text1.gravity = Gravity.CENTER_HORIZONTAL
            text1.apply {
                layoutParams = TableRow.LayoutParams(
                    mywidth,
                    myheight
                )
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
            tbrow.addView(text1)

            // Set the third column of the table row
            text2.setId(i + 3)
            text2.setBackgroundColor(ContextCompat.getColor(this, R.color.colortablerow))
            text2.setText(predictions.toString())
            text2.setTextColor(ContextCompat.getColor(this, R.color.colortextdata))
            text2.setTextSize(18F)
            text2.setTypeface(text2.getTypeface(), Typeface.BOLD);
            text2.gravity = Gravity.CENTER_HORIZONTAL
            text2.apply {
                layoutParams = TableRow.LayoutParams(
                    mywidth,
                    myheight
                )
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
            tbrow.addView(text2)

            // add the tablerow to the table Layout

            table.addView(tbrow)
        }
    }
}