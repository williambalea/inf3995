package inf3995.bixiapplication.StationView.MainScreen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import inf3995.bixiapplication.StationView.StationList.ListStationActivity
import inf3995.bixiapplication.StationView.PredictionErrors.ErrorPredictionsActivity
import inf3995.bixiapplication.StationView.StationPredictions.GlobalPredictionsActivity
import inf3995.bixiapplication.StationView.StationStatistics.GlobalStatisticsActivity
import inf3995.test.bixiapplication.R

class MainScreenActivity : AppCompatActivity() {

    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btn4: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        btn1 = findViewById(R.id.button1)
        btn2 = findViewById(R.id.button2)
        btn3 = findViewById(R.id.button3)
        btn4 = findViewById(R.id.button4)

        btn1.setOnClickListener{
            val intent = Intent(this, ListStationActivity::class.java)
            startActivity(intent)
        }

        btn2.setOnClickListener{
            val intent = Intent(this, GlobalStatisticsActivity::class.java)
            startActivity(intent)
        }

        btn3.setOnClickListener{
            val intent = Intent(this, GlobalPredictionsActivity::class.java)
            startActivity(intent)
        }

        btn4.setOnClickListener{
            val intent = Intent(this, ErrorPredictionsActivity::class.java)
            startActivity(intent)
        }

    }


}