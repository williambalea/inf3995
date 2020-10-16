package inf3995.bixiapplication

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.station_list_activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.io.StringReader

class ListStationActivity : AppCompatActivity(), StationAdapter.ClickListener { //StationAdapter.ClickedItem
    private val TAG = "Stations List"
    //val ipAddress = "70.80.27.156"
    //val ipAddress = "135.19.27.218"
    //val port = "2000"
    //val port = "2001"
    //var itemListModal: ArrayList<Station>? = null

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

        requestConnectionWithServer(IpAddressDialog.ipAddressInput, IpAddressDialog.portInput)

        requestToServer(IpAddressDialog.ipAddressInput, IpAddressDialog.portInput)

        /*for(item in itemListModal!!){
            stationModalList.add(item)
        }

         */

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        stationAdapter = StationAdapter()   //(this)
        stationAdapter!!.setData(stationModalList, this)
        recyclerView.adapter = stationAdapter

    }


    override fun clickedStation(station: Station) {
        var itemModal = station
        // val itemView : MenuView.ItemView?=null
        Log.e("TAG", "===> " + itemModal.name)
       // var name = itemModal.name
        //startActivity(Intent(this@ListStationActivity, StationCoordinatesActivity::class.java).putExtra("data", itemModal))
        //startActivity(Intent(this@ListStationActivity, StatisticsStationActivity::class.java).putExtra("data", itemModal))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        var menuItem = menu!!.findItem(R.id.searchView)
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

    private fun requestToServer(ipAddress: String, port: String) {

        // get Server Ip adress
        val retrofit = Retrofit.Builder()
            .baseUrl("http://$ipAddress:$port/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val service: WebBixiService = retrofit.create(WebBixiService::class.java)
        val call: Call<String> = service.getAllStationCode()

        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>?,
                response: Response<String>?
            ) {
                Log.i(TAG, "Réponse 1 du Serveur: ${response?.body()}")

                val arrayStationType = object : TypeToken<Array<Station>>() {}.type
                val jObj: Array<Station> = Gson().fromJson(response?.body(), arrayStationType)
                for (item in jObj) {
                    stationModalList.add(item)
                    Log.i(TAG, "Array :${item}")
                }
            }

            /*
                call.enqueue(object : Callback<String> {
                   // @RequiresApi(Build.VERSION_CODES.KITKAT)
                    override fun onResponse( call: Call<String>?, response: Response<String>?) {
                       Log.i(TAG, "Réponse 1 du Serveur: ${response?.body()}")
                        /* Reading JSON with the streaming API  at https://github.com/cbeust/klaxon*/
                       val klaxon = Klaxon()
                       JsonReader(StringReader(response?.body())).use { reader ->
                           //val result = arrayListOf<Station>()
                           reader.beginArray {
                               while (reader.hasNext()) {
                                   val station = klaxon.parse<Station>(reader)
                                   if (station != null) {
                                       itemListModal.add(station)
                                   }
                               }
                           }
                       }

                    }

                 */
            override fun onFailure(call: Call<String>?, t: Throwable) {
            }
        })
    }

        /*
    private fun requestToServer (ipAddress:String, port:String) {
        var reponse_serveur:JsonObject

        // get Server Ip adress
        val retrofit = Retrofit.Builder()
            .baseUrl("http://$ipAddress:$port/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val service: WebBixiService = retrofit.create(WebBixiService::class.java)
        val call: Call<ArrayList<Station>> = service.getAllStationCode()

        call.enqueue(object : Callback<ArrayList<Station>> {
            // @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onResponse(
                call: Call<ArrayList<Station>>?,
                response: Response<ArrayList<Station>>?
            ) {
                Log.i(TAG, "Réponse 1 du Serveur: ${response?.body()}")
                //val gson = GsonBuilder().create()
                //val model = gson.fromJson(response?.body(), Array<Station>::class.java).toMutableList()
                // val packagesArray = gson.fromJson(response.toString(), Array<Station>::class.java).toMutableList()
                //for(item in model){
                //    stationModalList.add(item)
                //}
                itemListModal = response?.body()!!
                stationModalList = response?.body()!!
                //recyclerView.adapter = StationAdapter(response.body()!!)
            }
            override fun onFailure(call: Call<ArrayList<Station>>?, t: Throwable) {
            }
        })
    }
*/

    private fun requestConnectionWithServer(ipAddresss:String, ports:String) {

        // get Server Ip adress
        val retrofit2 = Retrofit.Builder()
            .baseUrl("http://$ipAddresss:$ports/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val service2: WebBixiService = retrofit2.create(WebBixiService::class.java)
        val call2: Call<String> = service2.getHelloWorld()

        call2.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG, "Réponse 2 du Serveur: ${response?.body()}")

            }
            override fun onFailure(call: Call<String>?, t: Throwable) {
            }
        })
    }

}
