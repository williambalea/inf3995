package inf3995.bixiapplication.StationView.PredictionErrors

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
import inf3995.bixiapplication.StationViewModel.StationLiveData.DataErrorResponse
import inf3995.bixiapplication.StationViewModel.WebBixiService
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_per_date_error_predictions.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class PerDatePredictionErrorsActivity : AppCompatActivity() {

    var myImage: ImageView? = null
    private val TAG = " Prediction Errors "
    lateinit var table: TableLayout
    var year = "2017"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_per_date_error_predictions)

        ErrorPredictionYear.text = year.toString()
        table = findViewById(R.id.main_table)
        myImage = findViewById(R.id.image)
        requestToServer(IpAddressDialog.ipAddressInput)
    }

    private fun requestToServer(ipAddress: String?) {
        // get check connexion with Server Hello from Server
        val retrofit = Retrofit.Builder()
            .baseUrl("https://$ipAddress/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().readTimeout(120, TimeUnit.SECONDS).build())
            .build()
        val service: WebBixiService = retrofit.create(WebBixiService::class.java)
        val call: Call<String> = service.getPredictionsErrors()

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG,"Response body of predictions errors from server  : ${response?.body()}")
                Log.i(TAG,"Response status  of predictions errors from server : ${response?.code()}")

                if((response?.code() == 404)){
                    val builder = AlertDialog.Builder(this@PerDatePredictionErrorsActivity)
                    builder.setTitle("Oups. Error!")
                        .setMessage("Prediction is not yet done, please go to predictions page first.")
                    builder.setIcon(R.mipmap.ic_launcher)
                    builder.show().setOnDismissListener {
                        val intent = Intent(this@PerDatePredictionErrorsActivity, MainScreenActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP;
                        startActivity(intent)
                    }
                    } else {
                    val arrayStationType = object : TypeToken<DataErrorResponse>() {}.type
                    val jObj: DataErrorResponse = Gson().fromJson(
                        response?.body(),
                        arrayStationType
                    )
                    Log.i(TAG, "Object : $jObj")
                    fillData(jObj)
                    lllProgressBarPerD.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(
                    TAG,
                    "Error when receiving prediction errors !    cause:${t.cause}     message:${t.message}"
                )
                val builder = AlertDialog.Builder(this@PerDatePredictionErrorsActivity)
                builder.setTitle("Error while loading prediction errors!")
                    .setMessage("cause:${t.cause} \n message:${t.message}")
                builder.show()
            }

        })
    }
    private fun convertString64ToImage(base64String: String): Bitmap {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    private fun fillData(jObj: DataErrorResponse) {

        //precision = jObj.data.precision
        PrecisionPredictionValue.text = jObj.data.precision.toString()
        val myImageString = jObj.graph
        val image1 = findViewById(R.id.image) as ImageView
        try{image1.setImageBitmap(convertString64ToImage(myImageString))}
        catch (e: Exception){
            Log.e(TAG, "error")
        }
        Log.i(TAG, "Display the graph")

        for (i in 0 until jObj.data.errors.size ){
            val time = jObj.data.time[i]
            val errors = jObj.data.errors[i]

            val tbrow = TableRow(this)
            val text0 = TextView(this)
            val text1= TextView(this)
            val text2= TextView(this)

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
            text2.setText(errors.toString())
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

    override fun onResume() {
        super.onResume()

        MainScreenActivity.connectivity.observe(this, Observer {
            if (it[2] == "DOWN") {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Engine Error!").setMessage("There may be a problem with Engine 3")
                builder.show().setOnDismissListener {
                    val intent = Intent(this, MainScreenActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP;
                    startActivity(intent)
                }
            }
        })

    }
}