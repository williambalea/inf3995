package inf3995.bixiapplication.StationView.PredictionErrors

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_error_predictions.*
import kotlinx.android.synthetic.main.activity_global_predictions.spnPeriod
import kotlinx.android.synthetic.main.activity_global_predictions.spnTime

class ErrorPredictionsActivity : AppCompatActivity() {

    var time: String? = null
    var year: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error_predictions)

        val years_List = listOf("","2017", "2018", "2019","2020")
        val years_adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, years_List)
        spnTime.adapter=years_adapter
        val period_List = listOf("","perMonth", "perWeekDay", "perHour")
        val period_adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, period_List)
        spnPeriod.adapter = period_adapter
        limitDropDownmenuHeight(spnPeriod)
        limitDropDownmenuHeight(spnTime)

        spnTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = years_List[position]
                Toast.makeText(this@ErrorPredictionsActivity, "Year $item selected", Toast.LENGTH_SHORT).show()
                year = item
            }
        }

        spnPeriod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = period_List[position]
                Toast.makeText(this@ErrorPredictionsActivity, "Period $item selected", Toast.LENGTH_SHORT).show()
                time = item
            }
        }

        buttonDisplayPredictionErrors.setOnClickListener{
            when (time){
                "perMonth"-> {
                    val intent = Intent(this@ErrorPredictionsActivity, MonthlyPredictionErrorsActivity::class.java)
                    intent.putExtra("yearGlobal", year)
                    intent.putExtra("timeGlobal", time)
                    startActivity(intent)

                }
                "perWeekDay"-> {
                    val intent = Intent(this@ErrorPredictionsActivity, DailyPredictionErrorsActivity::class.java)
                    intent.putExtra("yearGlobal", year)
                    intent.putExtra("timeGlobal", time)
                    startActivity(intent)
                }
                "perHour"-> {
                    val intent = Intent(this@ErrorPredictionsActivity, HourlyPredictionErrorsActivity::class.java)
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