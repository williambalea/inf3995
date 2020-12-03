package inf3995.bixiapplication.StationView.Predictions.GlobalPredictions

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import inf3995.bixiapplication.StationView.MainScreen.MainScreenActivity
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_global_predictions.*
import kotlinx.android.synthetic.main.activity_station_statistics.display_button
import java.util.*


class GlobalPredictionsActivity : AppCompatActivity() {

    var time: String? = null
    var year: String? = "2017"
    var dateStart : String? = null
    var dateEnd : String? = null
    private val TAG = "Global Prediction Activity"
    var yearEnd = 2017
    var yearStart = 2017

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_predictions)

        val startDateButton = findViewById<Button>(R.id.startDateButton)
        val endDateButton = findViewById<Button>(R.id.endDateButton)

        // button click to show date picker
        startDateButton.setOnClickListener {
            // Initialize a new calendar instance
            val c = Calendar.getInstance()

            // Get the calendar current year, month and day of month
            val theYear = c.get(Calendar.YEAR)
            val theMonth = c.get(Calendar.MONTH)
            val theDay = c.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, mYear, mMonth, mDay ->
                    val mmMonth = mMonth + 1
                    startDate.text = "$mDay-$mmMonth-$mYear"
                    dateStart = "$mDay-$mmMonth-$mYear"
                    year = mYear.toString()
                },
                theYear,
                theMonth,
                theDay
            )
            datePicker.show()
        }

        endDateButton.setOnClickListener {
            // Initialize a new calendar instance
            val c = Calendar.getInstance()

            // Get the calendar current year, month and day of month
            val theYear = year!!.toInt()
            val theMonth = c.get(Calendar.MONTH)
            val theDay = c.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(
                this,
                { _, mYear, mMonth, mDay ->
                    val mmMonth = mMonth + 1
                    val date = "$mDay-$mmMonth-$mYear"
                    endDate.text = date
                    dateEnd = "$mDay-$mmMonth-$mYear"
                },
                theYear,
                theMonth,
                theDay
            )
            datePicker.show()
        }

        // Dropdownmenu  to choose de GroupByor period
        val period_List = listOf("", "perMonth", "perWeekDay", "perHour","perDate")
        val period_adapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            period_List
        )
        spnPeriode.adapter = period_adapter

        limitDropDownMenuHeight(spnPeriode)

        spnPeriode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item:String = period_List[position]
                time = item
            }
        }

        display_button.setOnClickListener{
            val annee = year
            val temps = time
            val dateEnd = dateEnd
            val dateStart= dateStart
            Log.i(TAG, "l'année est : $year")

            when (temps){
                "perDate" -> {
                    val intent = Intent(
                        this@GlobalPredictionsActivity,
                        PerDateGlobalPredictionActivity::class.java
                    )
                    intent.putExtra("Annee", annee)
                    intent.putExtra("Temps", temps)
                    intent.putExtra("DateStart", dateStart)
                    intent.putExtra("DateEnd", dateEnd)
                    startActivity(intent)
                }
                "perWeekDay" -> {
                    val intent = Intent(
                        this@GlobalPredictionsActivity,
                        DailyGlobalPredictionActivity::class.java
                    )
                    intent.putExtra("Annee", annee)
                    intent.putExtra("Temps", temps)
                    intent.putExtra("DateStart", dateStart)
                    intent.putExtra("DateEnd", dateEnd)
                    startActivity(intent)
                }
                "perHour" -> {
                    val intent = Intent(
                        this@GlobalPredictionsActivity,
                        HourlyGlobalPredictionActivity::class.java
                    )
                    intent.putExtra("Annee", annee)
                    intent.putExtra("Temps", temps)
                    intent.putExtra("DateStart", dateStart)
                    intent.putExtra("DateEnd", dateEnd)
                    startActivity(intent)
                }
                "perMonth" -> {
                    val intent = Intent(
                        this@GlobalPredictionsActivity,
                        MonthlyGlobalPredictionActivity::class.java
                    )
                    intent.putExtra("Annee", annee)
                    intent.putExtra("Temps", temps)
                    intent.putExtra("DateStart", dateStart)
                    intent.putExtra("DateEnd", dateEnd)
                    startActivity(intent)

                    Log.i(TAG, "Year is : $year")
                    Log.i(TAG, "Start date is  : $dateStart")
                    Log.i(TAG, "End date is : $dateEnd")
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()

        MainScreenActivity.connectivity.observe(this, Observer {
            if(it[2] == "DOWN"){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Engine Error!").setMessage("Connection with Engine 3 failed")

                    builder.show().setOnDismissListener {
                        val intent = Intent(this, MainScreenActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP;
                        startActivity(intent)
                    }
            }

        })

    }
    private fun limitDropDownMenuHeight(spnTest: Spinner){
        val popup = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible =true
        val popupWindow =popup.get(spnTest) as ListPopupWindow
        popupWindow.height = (5*resources.displayMetrics.density).toInt()
    }
}