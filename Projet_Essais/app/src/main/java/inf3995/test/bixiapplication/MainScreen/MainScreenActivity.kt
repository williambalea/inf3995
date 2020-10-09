package inf3995.test.bixiapplication.MainScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import inf3995.test.bixiapplication.R
import inf3995.test.bixiapplication.ListStationActivity

class MainScreenActivity : AppCompatActivity() {

    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btn4: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        btn1 = findViewById(R.id.button)
        btn2 = findViewById(R.id.button2)
        btn3 = findViewById(R.id.button3)
        btn4 = findViewById(R.id.button4)


        btn1.setOnClickListener{
            val intent = Intent(this, ListStationActivity::class.java)
            startActivity(intent)
        }



    }


}