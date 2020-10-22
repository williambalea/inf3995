package inf3995.bixiapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import inf3995.bixiapplication.Fragment.global_data_fragment
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_stations_data.*

class GlobalDataActivity : AppCompatActivity() {

    var temps: String? = null
    var annee: String? = null
    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_data)

        val yearsList = listOf("","2014", "2015", "2015")
        val yearsAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, yearsList)
        spnTime.adapter=yearsAdapter

        val periodList = listOf("","Monthly", "Daily", "Per Hour")
        val periodAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, periodList)
        spnPeriod.adapter = periodAdapter

        limitDropDownMenuHeight(spnPeriod)
        limitDropDownMenuHeight(spnTime)

        spnTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = yearsList[position]
                Toast.makeText(this@GlobalDataActivity, "Year $item selected", Toast.LENGTH_SHORT).show()
                annee = item
            }
        }

        spnPeriod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = periodList[position]
                Toast.makeText(this@GlobalDataActivity, "Period $item selected", Toast.LENGTH_SHORT).show()
                temps = item
            }
        }
    }

    fun limitDropDownMenuHeight(spnTest: Spinner){
        val popup = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible =true
        val popupWindow =popup.get(spnTest) as ListPopupWindow
        popupWindow.height = (5*resources.displayMetrics.density).toInt()
    }

    fun showGlobal_data_fragment(){
        val transaction = manager.beginTransaction()
        val fragment = global_data_fragment()
        transaction.replace(R.id.global_data_fragment_holder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
