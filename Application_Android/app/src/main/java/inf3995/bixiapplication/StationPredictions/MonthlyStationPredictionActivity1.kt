package inf3995.bixiapplication.StationPredictions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Base64.decode
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import inf3995.bixiapplication.Data.MonthlyStatisticStation
import inf3995.bixiapplication.Data.Station
import inf3995.bixiapplication.Dialog.IpAddressDialog
import inf3995.bixiapplication.Service.WebBixiService
import inf3995.bixiapplication.UnsafeOkHttpClient
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_monthly_station_prediction.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlinx.android.synthetic.main.activity_daily_station_prediction1.predictionYear as predictionYear1


class MonthlyStationPredictionActivity1 : AppCompatActivity() {

    var station : Station? = null
    lateinit var temps: String
    lateinit var indicator: String
    lateinit var call: Call<String>
    var code: Int = 0
    var year = 0
    var myImage:ImageView? = null
    private val TAG = "Monthly Station Predictions values"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_station_prediction1)
        val tempas = intent.getStringExtra("Temps")
        val indication = intent.getStringExtra("Indicateur")
        val annas = intent.getStringExtra("Annee")?.toInt()
       station = intent.getSerializableExtra("data") as Station

       Station_code.text = station!!.code.toString()
       Station_name.text = station!!.name

        if (tempas != null) {
            temps = tempas
        }

        if (indication != null) {
            indicator = indication
        }

        if (annas != null) {
            year = annas
        }
        predictionYear1.text = year.toString()

        /*
        if(indicator == "Value"){
            GraphTitle.text = "Predictions Values of Monthly Departures & Arrivals:"
        } else {
            "Predictions Errors of Monthly Departures & Arrivals:"
        }
         */

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
        val call: Call<String> = service.getStationStatistics(year, temps, code)
        /*
            if(indicator == "Value") {
              //val call: Call<String> = service.getStationPrediction(year, temps, code)
                val call: Call<String> = service.getStationStatistics(year, temps, code)

            }else{
                //val call: Call<String> = service.getStationErrors(year, temps, code)
                val call: Call<String> = service.getStationStatistics(year, temps, code)

            }
        */


        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG, "Réponse des Prédictions par mois du Serveur: ${response?.body()}")
                Log.i(TAG, "Status de reponse du Serveur: ${response?.code()}")

                val arrayStationType = object : TypeToken<MonthlyStatisticStation>() {}.type
                val jObj: MonthlyStatisticStation = Gson().fromJson(response?.body(), arrayStationType)
                Log.i(TAG, "L'objet : $jObj")
                fillData(jObj)
                lllProgressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG, "Error when receiving prediction values!    cause:${t.cause}     message:${t.message}")
                val builder = AlertDialog.Builder(this@MonthlyStationPredictionActivity1)
                builder.setTitle("Error while loading prediction values!").setMessage("cause:${t.cause} \n message:${t.message}")
                builder.show()
            }
        })
    }

    private fun fillData(jObj: MonthlyStatisticStation) {
        val myImageString = jObj.graph
        val image1 = findViewById(R.id.image) as ImageView
       try{image1.setImageBitmap(convertString64ToImage(myImageString))}
        catch (e:Exception){
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

    }

    private fun convertString64ToImage(base64String: String): Bitmap {
        val decodedString = decode(base64String, Base64.NO_WRAP)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

}