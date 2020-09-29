package inf3995.bixiapplication.station

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import inf3995.bixiapplication.R

class MainPageActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(
            R.layout.activtity_mainpage
        )
    }
}