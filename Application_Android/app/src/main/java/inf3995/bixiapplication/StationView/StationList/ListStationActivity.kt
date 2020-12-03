package inf3995.bixiapplication.StationView.StationList

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import inf3995.bixiapplication.StationView.Dialog.IpAddressDialog
import inf3995.bixiapplication.StationView.Dialog.UnsafeOkHttpClient
import inf3995.bixiapplication.StationView.MainScreen.MainScreenActivity
import inf3995.bixiapplication.StationViewModel.StationLiveData.Station
import inf3995.bixiapplication.StationViewModel.WebBixiService
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.station_list_activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class ListStationActivity : AppCompatActivity(){
    private val TAG = "Stations List"
    var stationModalList = ArrayList<Station>()
    var stationAdapter: StationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.station_list_activity_main)
        requestToServer(IpAddressDialog.ipAddressInput)
      
        station_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(filterString: String?): Boolean {
                stationAdapter!!.filter.filter(filterString)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                stationAdapter!!.filter.filter(newText)
                return false
            }

        })

        val searchIcon = station_search.findViewById<ImageView>(R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.BLACK)

        val cancelIcon = station_search.findViewById<ImageView>(R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.BLACK)

        val textView = station_search.findViewById<TextView>(R.id.search_src_text)
        textView.setTextColor(Color.BLACK)
        textView.textSize = 30F
    }

    private fun requestToServer(ipAddress: String?) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://$ipAddress")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().readTimeout(5, TimeUnit.SECONDS).build())
            .build()
        val service: WebBixiService = retrofit.create(WebBixiService::class.java)
        val call: Call<String> = service.getAllStationCode()

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                if(!response?.body().isNullOrBlank())
                    Log.i(TAG, "Response 1 from Server: ${response?.body()}")
                else
                    Log.i(TAG,"${response?.body()} --->   code:${response?.code()}    message:${response?.message()}")
                val arrayStationType = object : TypeToken<Array<Station>>() {}.type
                val jObj: Array<Station> = Gson().fromJson(response?.body(), arrayStationType)
                makeRecyclerView(jObj)
                llProgressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG, "Error when loading list of stations!    cause:${t.cause}     message:${t.message}")
                val builder = AlertDialog.Builder(this@ListStationActivity)
                builder.setTitle("Error while loading list of stations !").setMessage("cause:${t.cause} \n message:${t.message}")
                builder.show()
            }
        })
    }

    private fun makeRecyclerView(listStation: Array<Station>) {
        for(item in listStation){
            stationModalList.add(item)
            Log.i(TAG, "Array :${item}")
        }
        stationAdapter = StationAdapter()
        stationAdapter!!.setData(stationModalList, this@ListStationActivity)
        recyclerView.adapter = stationAdapter

        recyclerView.layoutManager = LinearLayoutManager(this@ListStationActivity)
        recyclerView.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()

        MainScreenActivity.connectivity.observe(this, Observer {
            if(it[0] == "DOWN"){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Engine Error!").setMessage("Connection with Engine 1 failed")
                builder.show().setOnDismissListener {
                    val intent = Intent(this, MainScreenActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP;
                    startActivity(intent)
                }
            }
        })
    }
}