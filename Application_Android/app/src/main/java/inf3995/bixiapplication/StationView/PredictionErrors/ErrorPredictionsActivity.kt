package inf3995.bixiapplication.StationView.PredictionErrors

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import inf3995.bixiapplication.StationView.MainScreen.MainScreenActivity
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_error_predictions.*

class ErrorPredictionsActivity : AppCompatActivity() {

    var year: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error_predictions)

        val displayButton = findViewById<Button>(R.id.buttonDisplayPredictionErrors)

        val years_List = listOf("","2017", "2018", "2019","2020")
        val years_adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, years_List)
        spnYear.adapter = years_adapter
        limitDropDownmenuHeight(spnYear)

        spnYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = years_List[position]
                Toast.makeText(this@ErrorPredictionsActivity, "Period $item selected", Toast.LENGTH_SHORT).show()
                year = item
            }
        }

        displayButton.setOnClickListener{

            val intent = Intent(this@ErrorPredictionsActivity, PerDatePredictionErrorsActivity::class.java)
            intent.putExtra("Year", year)
            startActivity(intent)

        }
    }
    override fun onResume() {
        super.onResume()

        MainScreenActivity.listen.observe(this, Observer {

            if(it[2] == "DOWN"){
                Toast.makeText(
                    this,
                    "Engine Problem!",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(this, MainScreenActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }

        })

    }
    fun limitDropDownmenuHeight(spnTest: Spinner){
        val popup = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible =true
        val popupWindow =popup.get(spnTest) as ListPopupWindow
        popupWindow.height = (5*resources.displayMetrics.density).toInt()
    }
}