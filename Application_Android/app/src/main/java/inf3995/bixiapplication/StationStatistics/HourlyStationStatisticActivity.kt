package inf3995.bixiapplication.StationStatistics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import inf3995.bixiapplication.Station
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_coordinates_station.*

class HourlyStationStatisticActivity : AppCompatActivity() {

    var station : Station?=null
    var temps: String? = null
    var annee: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_station_statistic)
        station = intent.getSerializableExtra("data") as Station
        Station_code.text = station!!.code.toString()
        Station_name.text = station!!.name

    }

}