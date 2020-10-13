package inf3995.test.bixiapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.station_items.*
import kotlinx.android.synthetic.main.station_list_activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class ListStationActivity : AppCompatActivity(), StationAdapter.ClickListener { //StationAdapter.ClickedItem
    private val TAG = "Stations List"
    val ipAddress = "70.80.27.156"
    val port = "2000"

    var itemListModal = arrayOf(
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
        Station(120, "Avenue papineau", 20.3744180F, 30.986466F)

    )
    var stationModalList = ArrayList<Station>()
    var stationAdapter: StationAdapter? = null
    private lateinit var dataButton: Button
    private lateinit var coordinatesButton: Button
    private lateinit var statisticsButton: Button
    private lateinit var predictionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.station_list_activity_main)


        for(item in itemListModal){
            stationModalList.add(item)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        stationAdapter = StationAdapter(this)
        stationAdapter!!.setData(stationModalList, this)
        recyclerView.adapter = stationAdapter

        requestConnectionWithServer()
        //requestToServer()
    }

    /*
    fun onClick(v: View?) {
        val itemView : MenuView.ItemView?=null
        dataButton = findViewById(R.id.data_button)
        coordinatesButton = findViewById(R.id.coordinates_button)
        statisticsButton = findViewById(R.id.statistics_button)
        predictionButton = findViewById(R.id.prediction_button)

        when(itemView){
            coordinates_button ->{
                Toast.makeText(this, "Coordinates Button clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DetailedStationActivity::class.java)
                startActivity(intent)
                true
            }
            data_button ->{
                Toast.makeText(this, "Data Button clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DataStationActivity::class.java)
                startActivity(intent)
                true
            }
            statistics_button ->{
                Toast.makeText(this, "Statistics Button clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, StatisticsStationActivity::class.java)
                startActivity(intent)
                true
            }
            prediction_button ->{
                Toast.makeText(this, "Predictions Button clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PredictionsStationActivity::class.java)       //.putExtra("data",itemModal)
                startActivity(intent)
                true
            }
            else -> false

        }

    }
    */

    override fun clickedStation(station: Station) {
        var itemModal = station
       // val itemView : MenuView.ItemView?=null
        Log.e("TAG","===> " + itemModal.name)
        var name = itemModal.name
        //startActivity(Intent(this@ListStationActivity, StationCoordinatesActivity::class.java).putExtra("data", itemModal))
        //startActivity(Intent(this@ListStationActivity, StatisticsStationActivity::class.java).putExtra("data", itemModal))
        /*
        when(itemView){
            coordinates_button ->{
                Toast.makeText(this, "Coordinates Button clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DetailedStationActivity::class.java).putExtra("data", itemModal)
                startActivity(intent)
                true
            }
            data_button ->{
                Toast.makeText(this, "Data Button clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DataStationActivity::class.java).putExtra("data", itemModal)
                startActivity(intent)
                true
            }
            statistics_button ->{
                Toast.makeText(this, "Statistics Button clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, StatisticsStationActivity::class.java).putExtra("data", itemModal)
                startActivity(intent)
                true
            }
            prediction_button ->{
                Toast.makeText(this, "Predictions Button clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PredictionsStationActivity::class.java).putExtra("data", itemModal)       //.putExtra("data",itemModal)
                startActivity(intent)
                true
            }
            else -> false
        }
        */

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        var menuItem = menu!!.findItem(R.id.searchView)
        val searchView = menuItem.actionView as SearchView

        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(filterString: String?): Boolean {
                stationAdapter!!.filter.filter(filterString)
               // Toast.makeText(this@MainActivity, "Looking for Station containing  $filterString in their name or code", Toast.LENGTH_LONG ).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                stationAdapter!!.filter.filter(newText)
               // Toast.makeText(this@MainActivity, "Looking for Station containing  $newText in their name or code", Toast.LENGTH_LONG ).show()
                return true
            }

        })

        return true
    }

    private fun requestToServer () {

        // get Server Ip adress
        val retrofit = Retrofit.Builder()
            .baseUrl("http://$ipAddress:$port/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val service: WebBixiService = retrofit.create(WebBixiService::class.java)
        val call: Call<List<Station>> = service.getAllStationCode()

        call.enqueue(object : Callback<List<Station>> {
            override fun onResponse(
                call: Call<List<Station>>?,
                response: Response<List<Station>>?
            ) {
                Log.i(TAG, "Réponse du Serveur: ${response?.body()}")
            }
            override fun onFailure(call: Call<List<Station>>?, t: Throwable) {
            }
        })
    }

    private fun requestConnectionWithServer() {

        // get Server Ip adress
        val retrofit2 = Retrofit.Builder()
            .baseUrl("http://$ipAddress:$port/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val service2: WebBixiService = retrofit2.create(WebBixiService::class.java)
        val call2: Call<String> = service2.getHelloWorld()

        call2.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG, "Réponse du Serveur: ${response?.body()}")
            }

            override fun onFailure(call: Call<String>?, t: Throwable) {
            }
        })
    }

}