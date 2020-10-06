package inf3995.bixiapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detailed_station.*

class DetailedStationActivity(nameStation:String, codeStation:Int) : AppCompatActivity() {

    private  lateinit var nameView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_station)
        val name = intent.getStringExtra("name")
        station_name.text = name

    }
}