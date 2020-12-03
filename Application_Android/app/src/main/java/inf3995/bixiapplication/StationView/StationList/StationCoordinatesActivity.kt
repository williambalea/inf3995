package inf3995.bixiapplication.StationView.StationList

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import inf3995.bixiapplication.StationView.MainScreen.MainScreenActivity
import inf3995.bixiapplication.StationViewModel.StationLiveData.Station
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_coordinates_station.*


class StationCoordinatesActivity : AppCompatActivity() {

    var station : Station?=null
    private lateinit var btn3: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinates_station)
        station = intent.getSerializableExtra("data") as Station
        Station_code.text = station!!.code.toString()
        Station_name.text = station!!.name
        Station_latitude.text = station!!.latitude.toString()
        Station_longitude.text = station!!.longitude.toString()

        btn3 = findViewById(R.id.button3)

        btn3.setOnClickListener{
            val intent = Intent(this, StationMapsActivity::class.java).putExtra("data", station)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        MainScreenActivity.connectivity.observe(this, Observer {

            if(it[0] == "DOWN"){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Engine Error!").setMessage("Connection with Engine 1 failed")
                builder.show().setOnDismissListener {
                    val intent = Intent(this, MainScreenActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP;
                    startActivity(intent)
                }
            }
        })

    }
}