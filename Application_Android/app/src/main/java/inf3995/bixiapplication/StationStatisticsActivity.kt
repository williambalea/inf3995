package inf3995.bixiapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.util.Base64
import android.widget.*
import inf3995.bixiapplication.StationStatistics.DailyStationStatisticActivity
import inf3995.bixiapplication.StationStatistics.HourlyStationStatisticActivity
import inf3995.bixiapplication.StationStatistics.MonthlyStationStatisticActivity
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_coordinates_station.*
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_code
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_name
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_code
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_name
import kotlinx.android.synthetic.main.activity_global_predictions.*
import kotlinx.android.synthetic.main.activity_global_predictions.spnPeriod
import kotlinx.android.synthetic.main.activity_global_predictions.spnTime
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.*
import kotlinx.android.synthetic.main.global_data_fragment.*
import java.io.ByteArrayOutputStream

class StationStatisticsActivity : AppCompatActivity() {

    var station : Station?=null
    var temps: String? = null
    var annee: String? = null
    var display_click: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station_statistics)
        station = intent.getSerializableExtra("data") as Station
        Station_code.text = station!!.code.toString()
        Station_name.text = station!!.name
       //val code = station!!.code
        val years_List = listOf("","2014", "2015", "2016")
        val years_adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, years_List)
        spnTime.adapter = years_adapter
        val period_List = listOf("","Monthly", "Daily", "Hourly")
        val period_adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, period_List)
        spnPeriod.adapter = period_adapter
       // limitDropDownmenuHeight(spnPeriod)
     //   limitDropDownmenuHeight(spnTime)

        spnTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = years_List[position]
                Toast.makeText(this@StationStatisticsActivity, "Year $item selected", Toast.LENGTH_SHORT).show()
                annee = item
            }
        }

        spnPeriod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = period_List[position]
                Toast.makeText(this@StationStatisticsActivity, "Period $item selected", Toast.LENGTH_SHORT).show()
                temps = item
            }
        }

        display_click = findViewById(R.id.display_button)

       /*
        display_click!!.setOnClickListener{
            val code = station!!.code
            val annee = annee
            val temps = temps
            val name = station!!.name
            val intent = Intent(this, MonthlyStationStatisticActivity::class.java)
            intent.putExtra("Code", code)
            intent.putExtra("Annee", annee)
            intent.putExtra("Name", name)
            intent.putExtra("Temps", temps)
            startActivity(intent)
        }
        */

       //val display_click = findViewById(R.id.display_button) as Button

        display_click!!.setOnClickListener{
            Toast.makeText(this," $station.name station Statistics", Toast.LENGTH_SHORT).show()
            val code = station!!.code
            val annee = annee
            val temps = temps
            val name = station!!.name
            when (temps){
                "Monthly"-> {
                    val intent = Intent(this@StationStatisticsActivity, MonthlyStationStatisticActivity::class.java)
                    intent.putExtra("Code", code)
                    intent.putExtra("Annee", annee)
                    intent.putExtra("Name", name)
                    intent.putExtra("Temps", temps)
                    startActivity(intent)

                }
                "Daily"-> {
                    val intent = Intent(this@StationStatisticsActivity, DailyStationStatisticActivity::class.java)
                    intent.putExtra("Code", code)
                    intent.putExtra("Annee", annee)
                    intent.putExtra("Name", name)
                    intent.putExtra("Temps", temps)
                    startActivity(intent)
                }
                "Hourly"-> {
                    val intent = Intent(this@StationStatisticsActivity, HourlyStationStatisticActivity::class.java)
                    intent.putExtra("Code", code)
                    intent.putExtra("Annee", annee)
                    intent.putExtra("Name", name)
                    intent.putExtra("Temps", temps)
                    startActivity(intent)
                }
            }
        }

    }

    fun limitDropDownmenuHeight(spnTest: Spinner){
        val popup = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible =true
        val popupWindow =popup.get(spnTest) as ListPopupWindow
        popupWindow.height = (5*resources.displayMetrics.density).toInt()
    }


}
