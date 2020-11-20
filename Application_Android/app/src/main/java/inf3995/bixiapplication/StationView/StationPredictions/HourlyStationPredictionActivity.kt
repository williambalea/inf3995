package inf3995.bixiapplication.StationView.StationPredictions

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
import inf3995.bixiapplication.StationView.Dialog.IpAddressDialog
import inf3995.bixiapplication.StationView.Dialog.UnsafeOkHttpClient
import inf3995.bixiapplication.StationViewModel.StationLiveData.DataResponseStation
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
    lateinit var indicator:String
    var code: Int = 0
    var annee= 0
    var myImage: ImageView? = null
    var dateStart : String? = null
    var dateEnd : String? = null
    private val TAG = "Hourly Station Prediction"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hourly_station_prediction)
        station = intent.getSerializableExtra("data") as Station
        val tempas = intent.getStringExtra("Temps")
        val annas = intent.getStringExtra("Annee")?.toInt()
        val dataStart = intent.getStringExtra("DateStart")
        val dataEnd = intent.getStringExtra("DateEnd")

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
            dateEnd = dataStart
        }

        predictTitleH.text = getString(R.string.Hourly_Prediction_Title)
        predictionYearH.text = annee.toString()
        code =  station!!.code
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
        //val call: Call<String> = service.getStationPrediction(annee, temps, code, dateStart, dateEnd)
        val call: Call<String> = service.getStationStatistics(annee, temps, code)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG, "Réponse des predictions du Serveur: ${response?.body()}")
                Log.i(TAG, "Status de reponse  des predictions du Serveur: ${response?.code()}")
                Log.i(TAG,"Message de reponse  des predictions du Serveur: ${response?.message()}")

                val arrayStationType = object : TypeToken<DataResponseStation>() {}.type
                val jObj: DataResponseStation = Gson().fromJson(response?.body(), arrayStationType)
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

    private fun fillData(jObj: DataResponseStation) {
        val myImageString = jObj.graph
        val image1 = findViewById(R.id.image) as ImageView
        try{image1.setImageBitmap(convertString64ToImage(myImageString))}
        catch (e: Exception){
            Log.e(TAG,"error")
        }
        //image1.setImageBitmap(convertString64ToImage(myImageString))
        Log.i(TAG, "affichage du graphique ")

        text12.setText(jObj.data.departureValue[0].toString())
        text13.setText(jObj.data.arrivalValue[0].toString())
        text22.setText(jObj.data.departureValue[1].toString())
        text23.setText(jObj.data.arrivalValue[1].toString())
        text32.setText(jObj.data.departureValue[2].toString())
        text33.setText(jObj.data.arrivalValue[2].toString())
        text42.setText(jObj.data.departureValue[3].toString())
        text43.setText(jObj.data.arrivalValue[3].toString())
        text52.setText(jObj.data.departureValue[4].toString())
        text53.setText(jObj.data.arrivalValue[4].toString())
        text62.setText(jObj.data.departureValue[5].toString())
        text63.setText(jObj.data.arrivalValue[5].toString())
        text72.setText(jObj.data.departureValue[6].toString())
        text73.setText(jObj.data.arrivalValue[6].toString())
        text82.setText(jObj.data.departureValue[7].toString())
        text83.setText(jObj.data.arrivalValue[7].toString())
        text92.setText(jObj.data.departureValue[8].toString())
        text93.setText(jObj.data.arrivalValue[8].toString())
        text102.setText(jObj.data.departureValue[9].toString())
        text103.setText(jObj.data.arrivalValue[9].toString())
        text112.setText(jObj.data.departureValue[10].toString())
        text113.setText(jObj.data.arrivalValue[10].toString())
        text122.setText(jObj.data.departureValue[11].toString())
        text123.setText(jObj.data.arrivalValue[11].toString())
        text132.setText(jObj.data.departureValue[12].toString())
        text133.setText(jObj.data.arrivalValue[12].toString())
        text142.setText(jObj.data.departureValue[13].toString())
        text143.setText(jObj.data.arrivalValue[13].toString())
        text152.setText(jObj.data.departureValue[14].toString())
        text153.setText(jObj.data.arrivalValue[14].toString())
        text162.setText(jObj.data.departureValue[15].toString())
        text163.setText(jObj.data.arrivalValue[15].toString())
        text172.setText(jObj.data.departureValue[16].toString())
        text173.setText(jObj.data.arrivalValue[16].toString())
        text182.setText(jObj.data.departureValue[17].toString())
        text183.setText(jObj.data.arrivalValue[17].toString())
        text192.setText(jObj.data.departureValue[18].toString())
        text193.setText(jObj.data.arrivalValue[18].toString())
        text202.setText(jObj.data.departureValue[19].toString())
        text203.setText(jObj.data.arrivalValue[19].toString())
        text212.setText(jObj.data.departureValue[20].toString())
        text213.setText(jObj.data.arrivalValue[20].toString())
        text222.setText(jObj.data.departureValue[21].toString())
        text223.setText(jObj.data.arrivalValue[21].toString())
        text232.setText(jObj.data.departureValue[22].toString())
        text233.setText(jObj.data.arrivalValue[22].toString())
        text242.setText(jObj.data.departureValue[23].toString())
        text243.setText(jObj.data.arrivalValue[23].toString())

    }

}