package inf3995.bixiapplication.StationView.Predictions.GlobalPredictions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_daily_station_prediction_global.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class DailyStationGlobalPredictionActivity : AppCompatActivity(){
    var station : Station? = null
    lateinit var temps: String
    lateinit var indicator:String
    var code: Int = 0
    var annee= 0
    var myImage: ImageView? = null
    var dateStart : String? = null
    var dateEnd : String? = null
    private val TAG = "Daily Station Prediction "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_station_prediction_global)
        val tempas = intent.getStringExtra("Temps")
        val indicat = intent.getStringExtra("Indicateur")
        val annas = intent.getStringExtra("Annee")?.toInt()
        val dataStart = intent.getStringExtra("DateStart")
        val dataEnd = intent.getStringExtra("DateEnd")

        if (tempas != null) {
            temps = tempas
        }

        if (indicat != null) {
            indicator = indicat
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
        //val call: Call<String> = service.getStationStatistics(annee, temps, code, dateStart, dateEnd)
        val call: Call<String>
        call = if(indicator == "Value") {
            //val call: Call<String> = service.getStationPrediction(annee, temps, code, dateStart, dateEnd)
            service.getStationStatistics(annee, temps, code)

        }else {
            //val call: Call<String> = service.getStationErrors(annee, temps, code, dateStart, dateEnd)
            service.getStationStatistics(annee, temps, code)
        }
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG, "Réponse des predictions du Serveur: ${response?.body()}")
                Log.i(TAG, "Status de reponse  des predictions du Serveur: ${response?.code()}")

                val arrayStationType = object : TypeToken<DataResponseStation>() {}.type
                val jObj: DataResponseStation = Gson().fromJson(response?.body(), arrayStationType)
                Log.i(TAG, "L'objet : $jObj")
                fillData(jObj)
                //lllProgressBarD.visibility = View.GONE
            }

            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG, "Error when receiving prediction!    cause:${t.cause}     message:${t.message}")
                val builder = AlertDialog.Builder(this@DailyStationGlobalPredictionActivity)
                builder.setTitle("Error while loading prediction!").setMessage("cause:${t.cause} \n message:${t.message}")
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

    }
}