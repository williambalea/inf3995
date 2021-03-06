package inf3995.bixiapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import inf3995.bixiapplication.StationStatistics.*
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_global_predictions.spnPeriod
import kotlinx.android.synthetic.main.activity_global_predictions.spnTime
import kotlinx.android.synthetic.main.activity_global_statistics.*

class GlobalStatisticsActivity : AppCompatActivity() {
    var time: String? = null
    var year: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_statistics)
        val years_List = listOf("","2014", "2015", "2016","2017")
        val years_adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, years_List)
        spnTime.adapter = years_adapter
        val period_List = listOf("","perMonth", "perWeekDay","perHour")
        val period_adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, period_List)
        spnPeriod.adapter = period_adapter
        limitDropDownmenuHeight(spnPeriod)
        limitDropDownmenuHeight(spnTime)

        spnTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = years_List[position]
                Toast.makeText(this@GlobalStatisticsActivity, "Year $item selected", Toast.LENGTH_SHORT).show()
                year = item
            }
        }

        spnPeriod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = period_List[position]
                Toast.makeText(this@GlobalStatisticsActivity, "Period $item selected", Toast.LENGTH_SHORT).show()
                time = item
            }
        }

        buttonDisplayGlobal.setOnClickListener{
            when (time){
                "perMonth"-> {
                    val intent = Intent(this@GlobalStatisticsActivity, MonthlyStationStatisticGlobalActivity::class.java)
                    intent.putExtra("yearGlobal", year)
                    intent.putExtra("timeGlobal", time)
                    startActivity(intent)

                }
                "perWeekDay"-> {
                    val intent = Intent(this@GlobalStatisticsActivity, DailyStationStatisticGlobalActivity::class.java)
                    intent.putExtra("yearGlobal", year)
                    intent.putExtra("timeGlobal", time)
                    startActivity(intent)
                }
                "perHour"-> {
                    val intent = Intent(this@GlobalStatisticsActivity, HourlyStationStatisticGlobalActivity::class.java)
                    intent.putExtra("yearGlobal", year)
                    intent.putExtra("timeGlobal", time)
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