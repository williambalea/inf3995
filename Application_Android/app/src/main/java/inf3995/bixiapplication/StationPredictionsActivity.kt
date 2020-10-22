package inf3995.bixiapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import inf3995.bixiapplication.Fragment.global_prediction_fragment
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_coordinates_station.*
import kotlinx.android.synthetic.main.activity_global_predictions.*

class StationPredictionsActivity : AppCompatActivity() {

    var station : Station?=null
    var temps: String? = null
    var annee: String? = null
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
        limitDropDownmenuHeight(spnPeriod)
        limitDropDownmenuHeight(spnTime)

        spnTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = years_List[position]
                Toast.makeText(this@StationPredictionsActivity, "Year $item selected", Toast.LENGTH_SHORT).show()
                annee = item
            }
        }

        spnPeriod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = period_List[position]
                Toast.makeText(this@StationPredictionsActivity, "Period $item selected", Toast.LENGTH_SHORT).show()
                temps = item
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
