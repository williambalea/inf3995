package inf3995.bixiapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import inf3995.bixiapplication.Data.Station
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
            val intent = Intent(this, StationLocalisation::class.java).putExtra("data", station)
            startActivity(intent)
        }
    }
}