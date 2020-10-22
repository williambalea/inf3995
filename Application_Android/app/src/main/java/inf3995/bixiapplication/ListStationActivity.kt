package inf3995.bixiapplication

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.station_list_activity_main.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class ListStationActivity : AppCompatActivity(){ //StationAdapter.ClickedItem

    private val TAG = "Stations List"
    var stationModalList = ArrayList<Station>()
    var stationAdapter: StationAdapter? = null
    private lateinit var dataButton: Button
    private lateinit var coordinatesButton: Button
    private lateinit var statisticsButton: Button
    private lateinit var predictionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.station_list_activity_main)
        requestToServer(IpAddressDialog.ipAddressInput)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val menuItem = menu!!.findItem(R.id.searchView)
        val searchView = menuItem.actionView as SearchView

        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(filterString: String?): Boolean {
                stationAdapter!!.filter.filter(filterString)
                Toast.makeText(
                    this@ListStationActivity,
                    "Looking for Station containing the word  $filterString ",
                    Toast.LENGTH_LONG
                ).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                stationAdapter!!.filter.filter(newText)
                Toast.makeText(
                    this@ListStationActivity,
                    "Looking for Station containing the word  $newText ",
                    Toast.LENGTH_LONG
                ).show()
                return true
            }
        })

        return true
    }

    private fun requestToServer(ipAddress: String) {
        // get check connexion with Server Hello from Server

        val retrofit = Retrofit.Builder()
            .baseUrl("https://$ipAddress")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().build())
            .build()
        val service: WebBixiService = retrofit.create(WebBixiService::class.java)
        val call: Call<String> = service.getAllStationCode()

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG, "Réponse 1 du Serveur: ${response?.body()}    code:${response?.code()}   message:${response?.message()}")
                val arrayStationType = object : TypeToken<Array<Station>>() {}.type
                val jObj: Array<Station> = Gson().fromJson(response?.body(), arrayStationType)
                makeRecyclerView(jObj)
                llProgressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG, "Error when receiving list of stations !!!    cause:${t.cause}     message:${t.message}")
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

}