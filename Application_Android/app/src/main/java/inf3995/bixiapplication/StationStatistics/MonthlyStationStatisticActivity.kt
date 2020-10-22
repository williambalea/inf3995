package inf3995.bixiapplication.StationStatistics

import android.app.Notification
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.StyleSpan
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import inf3995.bixiapplication.*
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_coordinates_station.*
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_code
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_name
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.*
import kotlinx.android.synthetic.main.station_list_activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class MonthlyStationStatisticActivity : AppCompatActivity() {

    lateinit var station : Station
    lateinit var temps: String
    var code: Int? = null
    var annee: Int? = null
    lateinit var myImageString:String
    private val TAG = "Monthly Station Statistics"
    var myTableLayout:TableLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_station_statistic)
        val codas = intent.getStringExtra("Code")?.toInt()
        val tempas = intent.getStringExtra("Temps")
        val annas = intent.getStringExtra("Annee")?.toInt()
        station = intent.getSerializableExtra("data") as Station

        Station_code.text = station.code.toString()
        Station_name.text = station.name

        if (tempas != null) {
            temps = tempas
        }
        code = codas
        annee = annas
        //image.setImageBitmap(MonthlyStationStatisticActivity.Base64Util.convertStringToBitmap(getString(R.string.helmets)))

        myTableLayout = findViewById(R.id.table_main)
        myImageString = findViewById(R.id.image)
        requestToServer(IpAddressDialog.ipAddressInput)
        image.setImageBitmap(Base64Util.convertStringToBitmap(myImageString))
    }



    private fun requestToServer(ipAddress: String) {

        // get check connexion with Server Hello from Server
        val retrofit = Retrofit.Builder()
            .baseUrl("https://$ipAddress/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().build())
            .build()
        val service: WebBixiService = retrofit.create(WebBixiService::class.java)
        val call: Call<String> = service.getStationStatistics(annee!!, temps, code!!)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG, "Réponse des Statistiques du Serveur: ${response?.body()}")
                val arrayStationType = object : TypeToken<MonthlyStatisticStation>() {}.type
                val jObj: MonthlyStatisticStation = Gson().fromJson(response?.body(), arrayStationType)
                fillmyTablelayout(jObj)

            }
            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG, "Echec de connexion avec le serveur !!!")
            }
        })
    }
    private fun fillmyTablelayout(listStation: MonthlyStatisticStation) {
        val tabrow:TableRow? =null
        var count = 1
        var stringTemp:String? = null
        var stringdeparture:String? = null
        var stringArrival:String? = null
        // Table Headers
        myImageString = listStation.graphique

        for (item in listStation.donnees) {
            stringTemp = item.temps
            stringdeparture = item.departureValue.toString()
            stringArrival = item.arrivalValue.toString()

            val tabrow0: TextView? = null
            tabrow0!!.setText(count)
            tabrow0.setTextColor(Color.BLACK)
            tabrow!!.addView(tabrow0)

            val tabrow1: TextView? = null
            tabrow1!!.setText(stringTemp)
            tabrow1.setTextColor(Color.BLACK)
            tabrow.addView(tabrow1)

            val tabrow2: TextView? = null
            tabrow2!!.setText(stringdeparture)
            tabrow2.setTextColor(Color.BLACK)
            tabrow.addView(tabrow2)

            val tabrow3: TextView? = null
            tabrow3!!.setText(stringArrival)
            tabrow3.setTextColor(Color.BLACK)
            tabrow.addView(tabrow3)

            myTableLayout?.addView(tabrow)
            count +=1
        }
    }


    object Base64Util {
        private val IMG_WIDTH = 640
        private val IMG_HEIGHT = 480
        private fun resizeBase64Image(base64image: String): String {
            val encodeByte: ByteArray = Base64.decode(base64image.toByteArray(), Base64.DEFAULT)
            val options = BitmapFactory.Options()
            var image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size, options)
            /* if (image.height &lt;= 400 &amp;&amp; image.width &lt;= 400) {
                return base64image
            }*/

            image = Bitmap.createScaledBitmap(image, IMG_WIDTH, IMG_HEIGHT, false)
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            System.gc()
            return Base64.encodeToString(b, Base64.NO_WRAP)
        }

        private fun convertString64ToImage(base64String: String): Bitmap {
            val decodedString = Base64.decode(base64String, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        }

        fun convertStringToBitmap(base64String: String): Bitmap {
            return convertString64ToImage(resizeBase64Image(base64String))
        }
    }


}