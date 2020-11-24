package inf3995.bixiapplication.StationView.Predictions.GlobalPredictions

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import inf3995.bixiapplication.StationViewModel.StationLiveData.Station
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_global_predictions.*
import java.util.*

class GlobalPredictionsActivity : AppCompatActivity() {

    var station : Station?=null
    var time: String? = null
    var indicator:String? = null
    var year: String? = null
    var dateStart : String? = null
    var dateEnd : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_predictions)

        // calendar
        val c1 = Calendar.getInstance()
        val yearDate1 = c1.get(Calendar.YEAR)
        val monthDate1 = c1.get(Calendar.MONTH)
        val dayDate1 = c1.get(Calendar.DAY_OF_MONTH)

        val c2 = Calendar.getInstance()
        val yearDate2 = c2.get(Calendar.YEAR)
        val monthDate2 = c2.get(Calendar.MONTH)
        val dayDate2 = c2.get(Calendar.DAY_OF_MONTH)


        // button click to show date picker
        startDateButton.setOnClickListener {
            val dpd = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener{ _, mYear, mMonth, mDay ->
                startDate.setText(""+ mDay+"/" + mMonth + "/"+mYear)}, yearDate1,monthDate1,dayDate1)
            dpd.show()
            dateStart = dpd.toString()
        }
        endDateButton.setOnClickListener {
            val dpd = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener{ _, mYear, mMonth, mDay ->
                endDate.setText(""+ mDay+"/" + mMonth + "/"+mYear)}, yearDate2,monthDate2,dayDate2)
            dpd.show()
            dateEnd = dpd.toString()

        }

        // Differents dropdownmenu

        val years_List = listOf("2017")
        val years_adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, years_List)
        spnTimes.adapter = years_adapter

        val period_List = listOf("","perMonth", "perWeekDay", "perHour")
        val period_adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, period_List)
        spnPeriode.adapter = period_adapter

        val indicator_List = listOf("","Value", "Error")
        val indicator_adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, indicator_List)
        spnIndicator.adapter = indicator_adapter

        limitDropDownmenuHeight(spnPeriode)
        limitDropDownmenuHeight(spnTimes)
        limitDropDownmenuHeight(spnIndicator)

        spnTimes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = years_List[position]
                Toast.makeText(this@GlobalPredictionsActivity, "Year $item selected", Toast.LENGTH_SHORT).show()
                year = item
            }
        }

        spnPeriode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = period_List[position]
                Toast.makeText(this@GlobalPredictionsActivity, "Period $item selected", Toast.LENGTH_SHORT).show()
                time = item
            }
        }

        spnIndicator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item:String = indicator_List[position]
                Toast.makeText(this@GlobalPredictionsActivity, "Indicator $item selected", Toast.LENGTH_SHORT).show()
                indicator = item
            }
        }

        display_button.setOnClickListener{
            Toast.makeText(this," $station.name station Predictions", Toast.LENGTH_SHORT).show()
            //val code = station!!.code
            val annee = year
            val temps = time
            val indicateur = indicator
            when(indicateur){
                "Value"->{
                    when (temps){
                        "perMonth"-> {
                            val intent = Intent(this@GlobalPredictionsActivity, MonthlyStationGlobalPredictionActivity::class.java)
                            intent.putExtra("Annee", annee)
                            intent.putExtra("Temps", temps)
                            intent.putExtra("Indicateur", indicateur)
                            intent.putExtra("DateStart", dateStart)
                            intent.putExtra("DateEnd", dateEnd)
                            startActivity(intent)

                        }
                        "perWeekDay"-> {
                            val intent = Intent(this@GlobalPredictionsActivity, DailyStationGlobalPredictionActivity::class.java)
                            intent.putExtra("Annee", annee)
                            intent.putExtra("Temps", temps)
                            intent.putExtra("Indicateur", indicateur)
                            intent.putExtra("DateStart", dateStart)
                            intent.putExtra("DateEnd", dateEnd)
                            startActivity(intent)
                        }
                        "perHour"-> {
                            val intent = Intent(this@GlobalPredictionsActivity, HourlyStationGlobalPredictionActivity::class.java)
                            intent.putExtra("Annee", annee)
                            intent.putExtra("Temps", temps)
                            intent.putExtra("Indicateur", indicateur)
                            intent.putExtra("DateStart", dateStart)
                            intent.putExtra("DateEnd", dateEnd)
                            startActivity(intent)
                        }
                    }
                }
                "Error"->{
                    when (temps){
                        "perMonth"-> {
                            val intent = Intent(this@GlobalPredictionsActivity, MonthlyStationGlobalPredictionActivity::class.java)
                            intent.putExtra("Annee", annee)
                            intent.putExtra("Temps", temps)
                            intent.putExtra("Indicateur", indicateur)
                            intent.putExtra("DateStart", dateStart)
                            intent.putExtra("DateEnd", dateEnd)
                            startActivity(intent)

                        }
                        "perWeekDay"-> {
                            val intent = Intent(this@GlobalPredictionsActivity, DailyStationGlobalPredictionActivity::class.java)
                            intent.putExtra("Annee", annee)
                            intent.putExtra("Temps", temps)
                            intent.putExtra("Indicateur", indicateur)
                            intent.putExtra("DateStart", dateStart)
                            intent.putExtra("DateEnd", dateEnd)
                            startActivity(intent)
                        }
                        "perHour"-> {
                            val intent = Intent(this@GlobalPredictionsActivity, HourlyStationGlobalPredictionActivity::class.java)
                            intent.putExtra("Annee", annee)
                            intent.putExtra("Temps", temps)
                            intent.putExtra("Indicateur", indicateur)
                            intent.putExtra("DateStart", dateStart)
                            intent.putExtra("DateEnd", dateEnd)
                            startActivity(intent)
                        }
                    }
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