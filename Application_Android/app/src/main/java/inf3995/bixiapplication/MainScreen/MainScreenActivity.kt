package inf3995.bixiapplication.MainScreen

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import inf3995.bixiapplication.Dialog.IpAddressDialog
import inf3995.bixiapplication.GlobalPredictionsActivity
import inf3995.bixiapplication.GlobalStatisticsActivity
import inf3995.bixiapplication.ListStationActivity
import inf3995.bixiapplication.Service.WebBixiService
import inf3995.bixiapplication.UnsafeOkHttpClient
import inf3995.test.bixiapplication.R
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
    private lateinit var btn4: Button
    val dialog = IpAddressDialog()

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.setting, menu)
        val favButton = menu?.findItem(R.id.connectivity)?.icon;
        connectivityCheck(IpAddressDialog.ipAddressInput, favButton)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

            when(item.itemId) {
                R.id.changeIpAddress -> {
                    dialog.show(supportFragmentManager, null)
                }
            }

        return super.onOptionsItemSelected(item)
    }

    fun connectivityCheck(ipAddress: String, item: Drawable? ){
        Observable.interval(
            1, 10,
            TimeUnit.SECONDS
        )
            .observeOn(Schedulers.io())
            .subscribe {
                val upArrow = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_baseline_directions_bike_24,
                    null
                )
                val retrofit4 = Retrofit.Builder()
                    .baseUrl("https://$ipAddress")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().build())
                    .build()

                val service4: WebBixiService = retrofit4.create(WebBixiService::class.java)
                val call4: Call<String> = service4.getConnectivity()

                call4.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        Log.i(
                            inf3995.bixiapplication.Dialog.TAG,
                            "Réponse Connectivity: ${response?.body()}"
                        )
                        val connectivity = response?.body()?.split(" ")?.toTypedArray()

                        if (connectivity?.get(0) == "true" && connectivity[1] == "true" && connectivity[2] == "true") {
                            item?.setTint(Color.argb(255, 0, 255, 0))
                        } else {
                            item?.setTint(Color.argb(255, 255, 255, 0))
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