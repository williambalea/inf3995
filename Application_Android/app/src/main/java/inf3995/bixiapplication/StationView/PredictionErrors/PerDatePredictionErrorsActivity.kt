package inf3995.bixiapplication.StationView.PredictionErrors

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import inf3995.bixiapplication.StationView.MainScreen.MainScreenActivity
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_per_date_error_predictions.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class PerDatePredictionErrorsActivity : AppCompatActivity() {


    var myImage: ImageView? = null
    private val TAG = " Prediction Errors "
    lateinit var table: TableLayout
    var year: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_per_date_error_predictions)
        val yearReceived = intent.getStringExtra("Year")
        if (yearReceived != null) {
            year = yearReceived
        }
        ErrorPredictionYear.text = year.toString()
        table = findViewById(R.id.main_table)
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
}