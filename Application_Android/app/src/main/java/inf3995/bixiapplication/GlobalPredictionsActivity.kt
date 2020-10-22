package inf3995.bixiapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_global_predictions.*

class GlobalPredictionsActivity : AppCompatActivity() {

    var temps: String? = null
    var annee: String? = null
    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_predictions)

        val years_List = listOf("","2014", "2015", "2016")
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
                Toast.makeText(this@GlobalPredictionsActivity, "Year $item selected", Toast.LENGTH_SHORT).show()
                annee = item
            }
        }

        spnPeriod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = period_List[position]
                Toast.makeText(this@GlobalPredictionsActivity, "Period $item selected", Toast.LENGTH_SHORT).show()
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

    fun showGlobal_data_fragment(){
        val transaction = manager.beginTransaction()
       // val fragment = global_prediction_fragment()
      //  transaction.replace(R.id.global_data_fragment_holder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}