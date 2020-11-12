package inf3995.bixiapplication.MainScreen

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.PopupMenu
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.FragmentManager
import inf3995.bixiapplication.*
import inf3995.bixiapplication.Dialog.IpAddressDialog
import inf3995.bixiapplication.Service.WebBixiService
import inf3995.test.bixiapplication.*
import kotlinx.android.synthetic.main.setting_ip_address_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import rx.Observable
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainScreenActivity : AppCompatActivity() {

    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    val dialog = IpAddressDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        connectivityCheck(IpAddressDialog.ipAddressInput)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.setting, menu)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

            when(item.itemId) {
                R.id.changeIpAddress ->{
                    //val ip = IpAddressDialog.ipAddressInput
                    dialog.show(supportFragmentManager, null)
                    //dialog.editTextIpAddress.setText(ip)
                }
                /*R.id.connectivity -> {
                    val upArrow: Drawable = R.drawable.ic_baseline_directions_bike_24;
                    upArrow.colorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);
                }*/

                //R.id.action_popup_predictions ->
                //    context.startActivity(Intent(context, StationPredictionsActivity::class.java).putExtra("data", station))
            }

        return super.onOptionsItemSelected(item)
    }

    fun connectivityCheck(ipAddress: String){

        Observable.interval(
            1, 10,
            TimeUnit.SECONDS
        )
            .observeOn(Schedulers.io())
            .subscribe {
                val retrofit4 = Retrofit.Builder()
                    .baseUrl("https://$ipAddress")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().build())
                    .build()

                val service4: WebBixiService = retrofit4.create(WebBixiService::class.java)
                val call4: Call<String> = service4.getConnectivity()

                call4.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        Log.i(inf3995.bixiapplication.Dialog.TAG, "Réponse Connectivity: ${response?.body()}")
                        val connectivity = response?.body()?.split(" ")?.toTypedArray()
                        val upArrow = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_baseline_directions_bike_24,
                            null
                        )
                        if(connectivity?.get(0) == "true" && connectivity[1] == "true" && connectivity[2] == "true") {

                            upArrow?.setTint(Color.argb(255, 0, 255, 0))
                            supportActionBar!!.setHomeAsUpIndicator(upArrow)
                        }
                        else{
                            upArrow?.setTint(Color.argb(255, 0, 255, 0))
                            supportActionBar!!.setHomeAsUpIndicator(upArrow)
                        }
                    }
                    override fun onFailure(call: Call<String>?, t: Throwable) {
                        Log.i(
                            inf3995.bixiapplication.Dialog.TAG,
                            "Error when getting message from server!    cause: ${t.cause}     message: ${t.message}"
                        )
                    }
                })
            }


    }

}