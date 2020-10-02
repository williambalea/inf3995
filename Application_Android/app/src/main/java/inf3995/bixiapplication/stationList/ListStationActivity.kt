package inf3995.bixiapplication.stationList

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import inf3995.bixiapplication.DetailsStationActivity
import inf3995.bixiapplication.MainActivity
import inf3995.bixiapplication.R

class ListStationActivity: AppCompatActivity(), StationListAdapter.ClickListener  {

    val sampleStations = arrayOf<Station>(
        Station(1,"station_1", 11.244456F, 21.076599F),
        Station(2,"station_2", 12.374400F, 22.356466F),
        Station(3,"station_3", 13.365400F, 23.000466F),
        Station(4,"station_4", 14.321400F, 24.348466F),
        Station(5,"station_5", 15.374980F, 25.356466F),
        Station(6,"station_6", 16.3744180F, 26.986466F),
        Station(7,"station_7", 17.365400F, 27.000466F),
        Station(8,"station_8", 18.321400F, 28.348466F),
        Station(9,"station_9", 19.374980F, 29.356466F),
        Station(10,"station_10", 20.3744180F, 30.986466F)
    )

    private lateinit var stations: MutableList<Station>
    private lateinit var recyclerView: RecyclerView
    private lateinit var listAdapter: StationListAdapter

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activtity_list_station)

        for(item in sampleStations){
            stations.add(item)
        }
        listAdapter = StationListAdapter(this)
        //listAdapter = StationListAdapter(stations, this)
        listAdapter!!.setData(stations)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = listAdapter
    }

    fun onStationSelected(station:Station) {
        Log.e("TAG",station.name)
        when(station.name){
            "" -> startActivity(Intent(this@ListStationActivity,MainActivity::class.java))
            else -> {
                Toast.makeText(this, "No Action", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun clickedStation(station: Station) {
        Log.e("TAG", station.name)
        when (station.name) {
            "Station details" -> startActivity(
                Intent(
                    this@ListStationActivity,
                    DetailsStationActivity::class.java
                )
            )
            else -> {
                Toast.makeText(this, "No Action", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val menuStation = menu!!.findItem(R.id.searchView)
        val searchView = menuStation.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(filterString: String?): Boolean {
                listAdapter.filter.filter(filterString)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                listAdapter.filter.filter(newText)
                return true
            }
        })
        return true
    }

}