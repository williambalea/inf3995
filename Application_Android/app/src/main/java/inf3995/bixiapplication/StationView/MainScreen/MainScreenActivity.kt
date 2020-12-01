package inf3995.bixiapplication.StationView.MainScreen

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import inf3995.bixiapplication.Data.ConnectivityData
import inf3995.bixiapplication.Dialog.EngineConnectivityStatusDialog
import inf3995.bixiapplication.StationView.Dialog.IpAddressDialog
import inf3995.bixiapplication.StationView.Dialog.UnsafeOkHttpClient
import inf3995.bixiapplication.StationView.PredictionErrors.ErrorPredictionsActivity
import inf3995.bixiapplication.StationView.Predictions.GlobalPredictions.GlobalPredictionsActivity
import inf3995.bixiapplication.StationView.StationList.ListStationActivity
import inf3995.bixiapplication.StationView.Statistics.GlobalStatistics.GlobalStatisticsActivity
import inf3995.bixiapplication.StationViewModel.WebBixiService
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_main_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import rx.Observable
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainScreenActivity : AppCompatActivity() {

    companion object {
        var isInFront :Boolean = true
        //var `listen.value` :Array<String>? = null
        var connectivity : MutableLiveData<Array<String>> =  MutableLiveData<Array<String>>()
    }

    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btn4: Button
    private lateinit var jObj: ConnectivityData
    val dialog1 = IpAddressDialog()
    val dialog2 = EngineConnectivityStatusDialog()
    private val TAG = "Main Screen Activity"

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
    override fun onResume() {
        super.onResume()
        isInFront = true
    }

    override fun onPause() {
        super.onPause()
        isInFront = false
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
                    dialog1.show(supportFragmentManager, null)
                }
                R.id.connectivity -> {
                    dialog2.show(supportFragmentManager, null)
                }
            }

        return super.onOptionsItemSelected(item)
    }

    private fun connectivityCheck(ipAddress: String, item: Drawable?){
        val engineProblemNotification = ObjectAnimator.ofInt(
            item,
            "Tint",
            Color.YELLOW,
            Color.WHITE
        )

        engineProblemNotification.setEvaluator(ArgbEvaluator())
        engineProblemNotification.repeatCount = ValueAnimator.INFINITE
        engineProblemNotification.repeatMode = ValueAnimator.REVERSE

        Observable.interval(
            0, 10,
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

                        val connectivityStatus = object : TypeToken<ConnectivityData>() {}.type

                        jObj = if (response?.isSuccessful!!) Gson().fromJson(
                            response.body(),
                            connectivityStatus
                        ) else Gson().fromJson(
                            response.errorBody()?.charStream(),
                            connectivityStatus
                        )

                        Log.i(
                            TAG,
                            "Réponse Connectivity: ${jObj.message} ${response.code()}"
                        )

                        connectivity.value = jObj.message.split(" ").toTypedArray()

                        if (connectivity.value!![0] == "UP" && connectivity.value!![1] == "UP" && connectivity.value!![2] == "UP") {
                            engineProblemNotification.cancel()
                            item?.setTint(Color.argb(255, 0, 255, 0))
                        } else {
                            engineProblemNotification.start()
                        }
                        buttonStatus(connectivity.value!!)
                        EngineConnectivityStatusDialog.status1 = connectivity.value!![0]
                        EngineConnectivityStatusDialog.status2 = connectivity.value!![1]
                        EngineConnectivityStatusDialog.status3 = connectivity.value!![2]
                    }

                    override fun onFailure(call: Call<String>?, t: Throwable) {
                        Log.i(
                            TAG,
                            "Error when getting message from server!    cause: ${t.cause}     message: ${t.message}"
                        )
                    }
                })
            }
    }

    private fun buttonStatus(engineStatus: Array<String>){
        button1.isEnabled = engineStatus[0] != "DOWN"
        button2.isEnabled = engineStatus[1] != "DOWN"
        button3.isEnabled = engineStatus[2] != "DOWN"
        button4.isEnabled = engineStatus[2] != "DOWN"
    }

}