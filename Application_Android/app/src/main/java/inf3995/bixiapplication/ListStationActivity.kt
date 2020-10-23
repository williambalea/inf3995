package inf3995.bixiapplication

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.station_list_activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class ListStationActivity : AppCompatActivity(){ //StationAdapter.ClickedItem

        /*
        val ipAddress = "70.80.27.156"
        val ipAddress = "135.19.27.218"
        val port = "2000"
        val port = "2001" */
        /*
        var itemListModal = arrayListOf(
            Station(123, "Cote vertu Est", 11.244456F, 21.076599F),
            Station(452, "Langelier Est", 12.374400F, 22.356466F),
            Station(375, "Montreal Nord", 13.365400F, 23.000466F),
            Station(458, "Polytechnique", 14.321400F, 24.348466F),
            Station(589, "Montréal", 15.374980F, 25.356466F),
            Station(600, "Saint Laurent", 16.3744180F, 26.986466F),
            Station(987, "Saint michel", 17.365400F, 27.000466F),
            Station(876, "Cote des neiges", 18.321400F, 28.348466F),
            Station(239, "Centre ville", 49.374980F, 29.356466F),
            Station(376, "Montreal Ouest", 53.365400F, 23.000466F),
            Station(444, "ETS", 14.321400F, 24.348466F),
            Station(580, "Saint_constant", 35.374980F, 25.356466F),
            Station(600, "Bonaventure", 16.3744180F, 26.986466F),
            Station(917, "Boulevard Lavesque", 47.365400F, 27.000466F),
            Station(874, "Cote sainte catherine", 18.321400F, 28.348466F),
            Station(289, "Cote de Liesse", 19.374980F, 49.356466F),
            Station(120, "Avenue papineau", 20.3744180F, 30.986466F))
        */

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
        /*for (item in itemListModal){
            stationModalList.add(item)
        }*/
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
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().readTimeout(5, TimeUnit.SECONDS).build())
            .build()
        val service: WebBixiService = retrofit.create(WebBixiService::class.java)
        val call: Call<String> = service.getAllStationCode()

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                if(!response?.body().isNullOrBlank())
                    Log.i(TAG, "Réponse 1 du Serveur: ${response?.body()}")
                else
                    Log.i(TAG,"${response?.body()} --->   code:${response?.code()}    message:${response?.message()}")
                val arrayStationType = object : TypeToken<Array<Station>>() {}.type
                val jObj: Array<Station> = Gson().fromJson(response?.body(), arrayStationType)
                makeRecyclerView(jObj)
                llProgressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG, "Error when receiving list of stations!    cause:${t.cause}     message:${t.message}")
                val builder = AlertDialog.Builder(this@ListStationActivity)
                builder.setTitle("Error while loading station list!").setMessage("cause:${t.cause} \n message:${t.message}")
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

}