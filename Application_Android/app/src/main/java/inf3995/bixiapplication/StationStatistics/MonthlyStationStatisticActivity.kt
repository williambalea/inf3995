package inf3995.bixiapplication.StationStatistics

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Base64.decode
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import inf3995.bixiapplication.*
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_code
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_name
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.Exception


class MonthlyStationStatisticActivity : AppCompatActivity() {

    var station : Station? = null
    lateinit var temps: String
    var code: Int = 0
    var annee= 0
    var myImage:ImageView? = null
    private val TAG = "Monthly Station Statistics"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_station_statistic)
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
                var jObj: MonthlyStatisticStation = Gson().fromJson(response?.body(), arrayStationType)
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

    private fun fillData(jObj:MonthlyStatisticStation) {
        val myImageString = jObj.graph
        val image1 = findViewById(R.id.image) as ImageView
       // image1.setImageBitmap(Base64Util.convertStringToBitmap(myImageString))
       try{image1.setImageBitmap(convertString64ToImage(myImageString))}
        catch (e:Exception){
            Log.e(TAG,"error")
        }

        Log.i(TAG, "affichage du graphique ")

        //val text10 = findViewById(R.id.text10) as TextView
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

    /*
    fun fillmyTablelayout(listStation: MonthlyStatisticStation) {
        var myTableLayout = findViewById(R.id.table_main) as TableLayout
        var count = 1
        // Table Headers
        Log.i(TAG, "ligne 113 !!")
        val myImageString = listStation.graph

        Log.i(TAG, "$listStation!!")
        Log.i(TAG, "$myImageString!!")

      //  var tabrows = Array<TableRow>(12){index-> TableRow(this) }

        val tabrow0 = TextView(this)
        val tabrow1= TextView(this)
        val tabrow2= TextView(this)
        val tabrow3= TextView(this)

        for (item in listStation.donnees.time.indices) {
            val tabrow = TableRow(this)
            val stringTemp = listStation.donnees.time[item]
            val stringdeparture = listStation.donnees.departureValue[item].toString()
            val stringArrival = listStation.donnees.arrivalValue[item].toString()

            Log.i(TAG, "valeur tabrownwnddn!!")

            for (i in 0..12){

            }
            if (tabrow.getParent() != null) {
                (tabrow.getParent() as ViewGroup).removeView(tabrow) // <- fix
            }

            if (tabrow0.getParent() != null) {
                (tabrow0.getParent() as ViewGroup).removeView(tabrow0) // <- fix
            }
            tabrow0.text = count.toString()
            tabrow0.setTextColor(Color.BLACK)

            // TEXTVIEW

            tabrow.addView(tabrow0)
            Log.i(TAG, "valeur de tabrow0 ${tabrow0.text} !!!")


           // val tabrow1: TextView? = null
            if (tabrow1.getParent() != null) {
                (tabrow1.getParent() as ViewGroup).removeView(tabrow1) // <- fix
            }
            tabrow1.setText(stringTemp)
            tabrow1.setTextColor(Color.BLACK)
            tabrow.addView(tabrow1)

            if (tabrow2.getParent() != null) {
                (tabrow2.getParent() as ViewGroup).removeView(tabrow2) // <- fix
            }
            tabrow2.setText(stringdeparture)
            tabrow2.setTextColor(Color.BLACK)
            tabrow.addView(tabrow2)

            if (tabrow3.getParent() != null) {
                (tabrow3.getParent() as ViewGroup).removeView(tabrow3) // <- fix
            }
            tabrow3.setText(stringArrival)
            tabrow3.setTextColor(Color.BLACK)
            tabrow.addView(tabrow3)


            Log.i(TAG, "valeur de tabrow0 ${tabrow0.text} !!!")
            Log.i(TAG, "valeur tabrow1 ${tabrow1.text} !!")


            myTableLayout.addView(tabrow)
            count +=1
        }

        val imageByte = decode(myImageString, Base64.NO_WRAP)
        val decodedImage = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.size)
        image.setImageBitmap(decodedImage)
    }

     */

/*
 fun getTextView (id: Int, title:String, color: Int, typeface:Int): TextView{
        val textView = TextView(this)
        textView.setId(id)
        textView.setText(title)
        textView.setTextColor(color)
        textView.setTypeface(Typeface.DEFAULT, typeface)
        textView.setLayoutParams(getLayOutParams())
        return textView

        }


    fun addData(jObj:MonthlyStatisticStation){
      val myTableLayout = findViewById(R.id.table_main) as TableLayout
        for (item in jObj.donnees.time.indices) {
            val tabrow = TableRow(this)
            tabrow.setLayoutParams(getLayOutParams())
            tabrow.addView(getTextView(item, (item +1).toString(),Color.BLACK, Typeface.NORMAL))
            tabrow.addView(getTextView(item,jObj.donnees.time[item],Color.BLACK, Typeface.NORMAL))
            tabrow.addView(getTextView(item,jObj.donnees.departureValue[item].toString(),Color.BLACK, Typeface.NORMAL))
            tabrow.addView(getTextView(item,jObj.donnees.arrivalValue[item].toString(),Color.BLACK, Typeface.NORMAL))
            myTableLayout.addView(tabrow)
        }
        val myImageString = jObj.graph
        val image1 = findViewById(R.id.image) as ImageView
        image1.setImageBitmap(Base64Util.convertStringToBitmap(myImageString))
        Log.i(TAG, "Ajout des donnees de la table")
    }

    fun getLayOutParams(): LinearLayout.LayoutParams{
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(10,10,10,10)
        return params
    }
    /*
        android:id="@+id/table_main"
        android:layout_width="match_parent"
        android:layout_height="530dp"
        android:layout_marginTop="0dp"
        android:paddingLeft="5dp"
        android:paddingRight="5dp">
    */

    fun getTableLayoutParams():TableLayout.LayoutParams{
        return TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
    }

    fun addHeaders(){
        val myTableLayout = findViewById(R.id.table_main) as TableLayout
        val tabrow = TableRow(this)
        tabrow.addView(getTextView(0, "Rank",Color.WHITE, Typeface.BOLD))
        tabrow.addView(getTextView(0,"Month",Color.BLACK, Typeface.NORMAL))
        tabrow.addView(getTextView(0,"Departures",Color.BLACK, Typeface.NORMAL))
        tabrow.addView(getTextView(0,"Arrivals",Color.BLACK, Typeface.NORMAL))
        myTableLayout.addView(tabrow, getTableLayoutParams())
        Log.i(TAG, " Ajout des entetes")
    }

 */


    private fun convertString64ToImage(base64String: String): Bitmap {
        val decodedString = decode(base64String, Base64.NO_WRAP)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    /*
object Base64Util {
        private val IMG_WIDTH = 640
        private val IMG_HEIGHT = 480
        private fun resizeBase64Image(base64image: String): String {
            val encodeByte: ByteArray = decode(base64image.toByteArray(), Base64.NO_WRAP)
            val options = BitmapFactory.Options()
            var image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size, options)

            image = Bitmap.createScaledBitmap(image, IMG_WIDTH, IMG_HEIGHT, false)
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            System.gc()
            return Base64.encodeToString(b, Base64.NO_WRAP)
        }

        private fun convertString64ToImage(base64String: String): Bitmap {
            val decodedString = decode(base64String, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        }

        fun convertStringToBitmap(base64String: String): Bitmap {
            return convertString64ToImage(resizeBase64Image(base64String))
        }
    }
     */


}