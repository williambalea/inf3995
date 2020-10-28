package inf3995.bixiapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import inf3995.bixiapplication.Dialog.IpAddressDialog
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val dialog = IpAddressDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        confirmButton.setOnClickListener { dialog.show(supportFragmentManager, null) }

    }

}