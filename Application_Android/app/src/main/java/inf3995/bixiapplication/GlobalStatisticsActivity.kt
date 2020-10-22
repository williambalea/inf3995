package inf3995.bixiapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import inf3995.test.bixiapplication.R

class GlobalStatisticsActivity : AppCompatActivity() {
    var temps: String? = null
    var annee: String? = null
    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_statistics)
    }
}