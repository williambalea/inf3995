package inf3995.bixiapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import inf3995.bixiapplication.Data.Station
import inf3995.bixiapplication.StationPredictions.DailyStationPredictionActivity
import inf3995.bixiapplication.StationPredictions.HourlyStationPredictionActivity
import inf3995.bixiapplication.StationPredictions.MonthlyStationPredictionActivity
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_code
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_name
import kotlinx.android.synthetic.main.activity_global_predictions.spnPeriod
import kotlinx.android.synthetic.main.activity_global_predictions.spnTime
import kotlinx.android.synthetic.main.activity_station_predictions.*

class StationPredictionsActivity : AppCompatActivity() {

    var station : Station?=null
    var time: String? = null
    var year: String? = null
    var indicator: String? = null
    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station_predictions)
        station = intent.getSerializableExtra("data") as Station
        Station_code.text = station!!.code.toString()
        Station_name.text = station!!.name


        val years_List = listOf("2017")
        val years_adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, years_List)
        spnTime.adapter=years_adapter
        val period_List = listOf("","Monthly", "Daily", "Per Hour")
        val period_adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, period_List)
        spnPeriod.adapter = period_adapter
        val indicator_List = listOf("","Value", "Error")
        val indicator_adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, indicator_List)
        spnIndicator.adapter=indicator_adapter

        limitDropDownmenuHeight(spnPeriod)
        limitDropDownmenuHeight(spnTime)
        limitDropDownmenuHeight(spnIndicator)

        spnTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = years_List[position]
                Toast.makeText(this@StationPredictionsActivity, "Year $item selected", Toast.LENGTH_SHORT).show()
                year = item
            }
        }

        spnPeriod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = period_List[position]
                Toast.makeText(this@StationPredictionsActivity, "Period $item selected", Toast.LENGTH_SHORT).show()
                time = item
            }
        }

        spnIndicator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = indicator_List[position]
                Toast.makeText(this@StationPredictionsActivity, "Indicator $item selected", Toast.LENGTH_SHORT).show()
                indicator = item
            }
        }

        display_button.setOnClickListener{
            Toast.makeText(this," $station.name station Statistics", Toast.LENGTH_SHORT).show()
            //val code = station!!.code
            val annee = year
            val temps = time
            val indicateur = indicator
            when (temps){
                "perMonth"-> {
                    val intent = Intent(this@StationPredictionsActivity, MonthlyStationPredictionActivity::class.java)
                    intent.putExtra("data", station)
                    intent.putExtra("Annee", annee)
                    intent.putExtra("Temps", temps)
                    intent.putExtra("Indicateur", indicateur)
                    startActivity(intent)

                }
                "perWeekDay"-> {
                    val intent = Intent(this@StationPredictionsActivity, DailyStationPredictionActivity::class.java)
                    intent.putExtra("data", station)
                    intent.putExtra("Annee", annee)
                    intent.putExtra("Temps", temps)
                    intent.putExtra("Indicateur", indicateur)
                    startActivity(intent)
                }
                "perHour"-> {
                    val intent = Intent(this@StationPredictionsActivity, HourlyStationPredictionActivity::class.java)
                    intent.putExtra("data", station)
                    intent.putExtra("Annee", annee)
                    intent.putExtra("Temps", temps)
                    intent.putExtra("Indicateur", indicateur)
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
