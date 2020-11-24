package inf3995.bixiapplication.StationView.Predictions.StationPredictions

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import inf3995.bixiapplication.StationView.MainScreen.MainScreenActivity
import inf3995.bixiapplication.StationViewModel.StationLiveData.Station
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_code
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_name
import kotlinx.android.synthetic.main.activity_station_predictions.*
import kotlinx.android.synthetic.main.activity_station_statistics.display_button
import java.util.*


class StationPredictionsActivity : AppCompatActivity() {

    var station : Station?=null
    var time: String? = null
    var year: String? = "2017"

    var dateStart : String? = null
    var dateEnd : String? = null

    private val TAG = "Station Prediction Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station_predictions)

        station = intent.getSerializableExtra("data") as Station
        Station_code.text = station!!.code.toString()
        Station_name.text = station!!.name

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
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    val mmMonth = mMonth + 1
                   startDate.setText("$mDay-$mmMonth-$mYear")
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
            val theYear = year!!.toInt() //c.get(Calendar.YEAR)
            val theMonth = c.get(Calendar.MONTH)
            val theDay = c.get(Calendar.DAY_OF_MONTH)
            val datepicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    val mmMonth = mMonth + 1
                    val date = "$mDay-$mmMonth-$mYear"
                    endDate.setText(date)           //setText(""+ mDay+"-" + mMonth + "-"+mYear)
                    dateEnd = "$mDay-$mmMonth-$mYear"
                },
                theYear,
                theMonth,
                theDay
            )
            datepicker.show()

        }


        // Dropdownmenu  to choose de GroupByor period

        val period_List = listOf("", "perMonth", "perWeekDay", "perHour","perDate")
        val period_adapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            period_List
        )
        spnPeriode.adapter = period_adapter

        limitDropDownmenuHeight(spnPeriode)

        spnPeriode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item:String = period_List[position]
                Toast.makeText(
                    this@StationPredictionsActivity,
                    "Period $item selected",
                    Toast.LENGTH_SHORT
                ).show()
                time = item
            }
        }

        display_button.setOnClickListener{
            Toast.makeText(this, " $station.name station Predictions", Toast.LENGTH_SHORT).show()
            //val code = station!!.code
            val annee = year
            val temps = time
            val dateEnd = dateEnd
            val dateStart= dateStart
            Log.i(TAG, "l'année est : $year")

            when (temps){
                "perMonth" -> {
                    val intent = Intent(
                        this@StationPredictionsActivity,
                        MonthlyStationPredictionActivity::class.java
                    )
                    intent.putExtra("data", station)
                    intent.putExtra("Annee", annee)
                    intent.putExtra("Temps", temps)
                    intent.putExtra("DateStart", dateStart)
                    intent.putExtra("DateEnd", dateEnd)
                    startActivity(intent)

                }
                "perWeekDay" -> {
                    val intent = Intent(
                        this@StationPredictionsActivity,
                        DailyStationPredictionActivity::class.java
                    )
                    intent.putExtra("data", station)
                    intent.putExtra("Annee", annee)
                    intent.putExtra("Temps", temps)
                    intent.putExtra("DateStart", dateStart)
                    intent.putExtra("DateEnd", dateEnd)
                    startActivity(intent)
                }
                "perHour" -> {
                    val intent = Intent(
                        this@StationPredictionsActivity,
                        HourlyStationPredictionActivity::class.java
                    )
                    intent.putExtra("data", station)
                    intent.putExtra("Annee", annee)
                    intent.putExtra("Temps", temps)
                    intent.putExtra("DateStart", dateStart)
                    intent.putExtra("DateEnd", dateEnd)
                    startActivity(intent)
                }
                "perDate" -> {
                    val intent = Intent(
                        this@StationPredictionsActivity,
                        PerDateStationPredictionActivity::class.java
                    )
                    intent.putExtra("data", station)
                    intent.putExtra("Annee", annee)
                    intent.putExtra("Temps", temps)
                    intent.putExtra("DateStart", dateStart)
                    intent.putExtra("DateEnd", dateEnd)
                    startActivity(intent)

                    Log.i(TAG, "l'année est : $year")
                    Log.i(TAG, "la date de début de la plage est : $dateStart")
                    Log.i(TAG, "la date de Fin de la plage est : $dateEnd")
                }
            }
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