package inf3995.bixiapplication.MainScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import inf3995.bixiapplication.GlobalPredictionsActivity
import inf3995.bixiapplication.GlobalStatisticsActivity
import inf3995.bixiapplication.ListStationActivity
import inf3995.test.bixiapplication.*

class MainScreenActivity : AppCompatActivity() {

    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        btn1 = findViewById(R.id.button1)
        btn2 = findViewById(R.id.button2)
        btn3 = findViewById(R.id.button3)


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

    }


}