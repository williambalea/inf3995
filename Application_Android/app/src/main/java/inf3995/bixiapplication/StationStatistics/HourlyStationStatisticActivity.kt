package inf3995.bixiapplication.StationStatistics

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import inf3995.bixiapplication.*
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_code
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_name
import kotlinx.android.synthetic.main.activity_hourly_station_statistic.*
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.*
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.image
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text102
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text103
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text112
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text113
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text12
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text122
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text123
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text13
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text22
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text23
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text32
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text33
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text42
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text43
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text52
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text53
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text62
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text63
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text72
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text73
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text82
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text83
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text92
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.text93
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.ByteArrayOutputStream
import kotlinx.android.synthetic.main.activity_hourly_station_statistic.statisticYear as statisticYear1

class HourlyStationStatisticActivity : AppCompatActivity() {
    var station : Station? = null
    lateinit var temps: String
    var code: Int = 0
    var annee= 0
    var myImage: ImageView? = null
    private val TAG = "Hourly Station Statistics"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hourly_station_statistic)
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


    private fun requestToServer(ipAddress: String?) {

        // get check connexion with Server Hello from Server
        val retrofit = Retrofit.Builder()
            .baseUrl("https://$ipAddress/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().build())
            .build()
        val service: WebBixiService = retrofit.create(WebBixiService::class.java)
        val call: Call<String> = service.getStationStatistics(annee, temps, code)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG, "Réponse des Statistiques du Serveur: ${response?.body()}")
                Log.i(TAG, "Status de reponse  des Statistiques du Serveur: ${response?.code()}")
                Log.i(
                    TAG,
                    "Message de reponse  des Statistiques du Serveur: ${response?.message()}"
                )

                val arrayStationType = object : TypeToken<MonthlyStatisticStation>() {}.type
                val jObj: MonthlyStatisticStation = Gson().fromJson(response?.body(), arrayStationType)
                Log.i(TAG, "L'objet : $jObj")
                //addHeaders()
                //addData(jObj)
                fillData(jObj)
                //fillmyTablelayout(jObj)
            }

            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG, "Echec de connexion avec le serveur !!!")
            }
        })
    }
        /*

 */
    private fun convertString64ToImage(base64String: String): Bitmap {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    private fun fillData(jObj: MonthlyStatisticStation) {
        val myImageString = jObj.graph
        val image1 = findViewById(R.id.image) as ImageView
      //  image1.setImageBitmap(Base64Util.convertStringToBitmap(myImageString))
        image1.setImageBitmap(convertString64ToImage(myImageString))
        Log.i(TAG, "affichage du graphique ")

        //val text10 = findViewById(R.id.text10) as TextView
        text12.setText(jObj.donnees.departureValue[0].toString())
        text13.setText(jObj.donnees.arrivalValue[0].toString())
        text22.setText(jObj.donnees.departureValue[1].toString())
        text23.setText(jObj.donnees.arrivalValue[1].toString())
        text32.setText(jObj.donnees.departureValue[2].toString())
        text33.setText(jObj.donnees.arrivalValue[2].toString())
        text42.setText(jObj.donnees.departureValue[3].toString())
        text43.setText(jObj.donnees.arrivalValue[3].toString())
        text52.setText(jObj.donnees.departureValue[4].toString())
        text53.setText(jObj.donnees.arrivalValue[4].toString())
        text62.setText(jObj.donnees.departureValue[5].toString())
        text63.setText(jObj.donnees.arrivalValue[5].toString())
        text72.setText(jObj.donnees.departureValue[6].toString())
        text73.setText(jObj.donnees.arrivalValue[6].toString())
        text82.setText(jObj.donnees.departureValue[7].toString())
        text83.setText(jObj.donnees.arrivalValue[7].toString())
        text92.setText(jObj.donnees.departureValue[8].toString())
        text93.setText(jObj.donnees.arrivalValue[8].toString())
        text102.setText(jObj.donnees.departureValue[9].toString())
        text103.setText(jObj.donnees.arrivalValue[9].toString())
        text112.setText(jObj.donnees.departureValue[10].toString())
        text113.setText(jObj.donnees.arrivalValue[10].toString())
        text122.setText(jObj.donnees.departureValue[11].toString())
        text123.setText(jObj.donnees.arrivalValue[11].toString())
        text132.setText(jObj.donnees.departureValue[12].toString())
        text133.setText(jObj.donnees.arrivalValue[12].toString())
        text142.setText(jObj.donnees.departureValue[13].toString())
        text143.setText(jObj.donnees.arrivalValue[13].toString())
        text152.setText(jObj.donnees.departureValue[14].toString())
        text153.setText(jObj.donnees.arrivalValue[14].toString())
        text162.setText(jObj.donnees.departureValue[15].toString())
        text163.setText(jObj.donnees.arrivalValue[15].toString())
        text172.setText(jObj.donnees.departureValue[16].toString())
        text173.setText(jObj.donnees.arrivalValue[16].toString())
        text182.setText(jObj.donnees.departureValue[17].toString())
        text183.setText(jObj.donnees.arrivalValue[17].toString())
        text192.setText(jObj.donnees.departureValue[18].toString())
        text193.setText(jObj.donnees.arrivalValue[18].toString())
        text202.setText(jObj.donnees.departureValue[19].toString())
        text203.setText(jObj.donnees.arrivalValue[19].toString())
        text212.setText(jObj.donnees.departureValue[20].toString())
        text213.setText(jObj.donnees.arrivalValue[20].toString())
        text222.setText(jObj.donnees.departureValue[21].toString())
        text223.setText(jObj.donnees.arrivalValue[21].toString())
        text232.setText(jObj.donnees.departureValue[22].toString())
        text233.setText(jObj.donnees.arrivalValue[22].toString())
        text242.setText(jObj.donnees.departureValue[23].toString())
        text243.setText(jObj.donnees.arrivalValue[23].toString())

    }

  /*
    object Base64Util {
        private val IMG_WIDTH = 640
        private val IMG_HEIGHT = 480
        private fun resizeBase64Image(base64image: String): String {
            val encodeByte: ByteArray = Base64.decode(base64image.toByteArray(), Base64.NO_WRAP)
            val options = BitmapFactory.Options()
            var image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size, options)

            image = Bitmap.createScaledBitmap(image, IMG_WIDTH, IMG_HEIGHT, false)
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            System.gc()
            return Base64.encodeToString(b, Base64.NO_PADDING)
        }

        private fun convertString64ToImage(base64String: String): Bitmap {
            val decodedString = Base64.decode(base64String, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        }

        fun convertStringToBitmap(base64String: String): Bitmap {
            return convertString64ToImage(resizeBase64Image(base64String))
        }
    }
   */

}