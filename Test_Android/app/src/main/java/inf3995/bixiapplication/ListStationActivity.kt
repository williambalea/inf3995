package inf3995.bixiapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class ListStationActivity : AppCompatActivity(), StationListAdapter.StationClickListener {

    val stations = ArrayList<Station>()
    val displayedStations = ArrayList<Station>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var progressbar: ProgressBar
    //private lateinit var stationList: MutableList<Station>
    var stationListAdapter: StationListAdapter? = null
    private lateinit var editTextSearch: EditText


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {

        super.onCreate(savedInstanceState, persistentState)
        Log.i("test","passed")
        setContentView(R.layout.activity_list_station)

        stations.add(Station(1, "station_1", 11.244456F, 21.076599F))
        stations.add(Station(2, "station_2", 12.374400F, 22.356466F))
        stations.add(Station(3, "station_3", 13.365400F, 23.000466F))
        stations.add(Station(4, "station_4", 14.321400F, 24.348466F))
        stations.add(Station(5, "station_5", 15.374980F, 25.356466F))
        stations.add(Station(6, "station_6", 16.3744180F, 26.986466F))
        stations.add(Station(7, "station_7", 17.365400F, 27.000466F))
        stations.add(Station(8, "station_8", 18.321400F, 28.348466F))
        stations.add(Station(9, "station_9", 19.374980F, 29.356466F))
        stations.add(Station(10, "station_10", 20.3744180F, 30.986466F))
        displayedStations.addAll(stations)

        stationListAdapter = StationListAdapter(this)
        stationListAdapter!!.setData(stations)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = stationListAdapter

    }

    override fun onStationClicked(station: Station) {
        Log.e("TAG", station.name)
        startActivity(Intent( this@ListStationActivity, DetailedStationActivity::class.java).putExtra(station.name, station.code))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)
        val menuStation = menu!!.findItem(R.id.searchView)

        if (menuStation != null) {
            val searchView = menuStation.actionView as SearchView

            val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            editText.hint= "Search ..."

            searchView.maxWidth = Int.MAX_VALUE

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(filterString: String?): Boolean {
                    stationListAdapter!!.filter.filter(filterString)
                    Toast.makeText(
                        this@ListStationActivity,
                        "Looking for $filterString",
                        Toast.LENGTH_LONG
                    ).show()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isNotEmpty()) {
                        displayedStations.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        stations.forEach {
                            if (it.name.toLowerCase(Locale.getDefault()).contains(search)) {
                                displayedStations.add(it)
                            }
                        }
                        recyclerView.adapter!!.notifyDataSetChanged()
                    } else {
                        displayedStations.clear()
                        displayedStations.addAll(stations)
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }
                /*  stationListAdapter!!.filter.filter(newText)
                    Toast.makeText(this@ListStationActivity, "Looking for $newText", Toast.LENGTH_LONG).show()
                    return true
                    */
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

}


/*
    override fun onOptionStationSelected(station: MenuStation): Boolean {
            // Toast.makeText(this@ListStationActivity, "Station" + position + "Clicked", Toast.LENGTH_LONG).show()
            val intent = Intent(this, DetailedStationActivity::class.java)
            intent.putExtra("name", stations[position].name)
            startActivity(intent)
    }
*/

    /*fun clickedStation(station: Station) {
            Log.e("TAG", station.name)
            startActivity(Intent( this@ListStationActivity, DetailedStationActivity::class.java).putExtra(station.name, station.code))
            // startActivity(Intent( this@ListStationActivity, DetailedStationActivity(station.name, station.code)::class.java))
        }
        /*
          recyclerView = findViewById(R.id.recyclerview)
          progressbar = findViewById(R.id.progress_circular)
          editTextSearch = findViewById(R.id.et_search)
           stationListAdapter.notifyDataSetChanged()
  */
      /*  getStationList()
        editTextSearch.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })*/

/*
        recyclerView = findViewById(R.id.recyclerview)
        toolbar = findViewById(R.id.toolbar)
        this.setSupportActionBar(toolbar)
        this.getSupportActionBar()?.setTitle("")

        stationListAdapter = StationListAdapter()
        stationListAdapter!!.setData(stations)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        //recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration( DividerItemDecoration(this,LinearLayoutManager.VERTICAL))
       // recyclerView.setHasFixedSize(true)
        recyclerView.adapter = stationListAdapter

        fun createDummyStationList(){

        stations.add(Station(1,"station_1", 11.244456F, 21.076599F))
        stations.add(Station(2,"station_2", 12.374400F, 22.356466F))
        stations.add(Station(3,"station_3", 13.365400F, 23.000466F))
        stations.add(Station(4,"station_4", 14.321400F, 24.348466F))
        stations.add(Station(5,"station_5", 15.374980F, 25.356466F))
        stations.add(Station(6,"station_6", 16.3744180F, 26.986466F))
        stations.add(Station(7,"station_7", 17.365400F, 27.000466F))
        stations.add(Station(8,"station_8", 18.321400F, 28.348466F))
        stations.add(Station(9,"station_9", 19.374980F, 29.356466F))
        stations.add(Station(10,"station_10", 20.3744180F, 30.986466F))
    }


    private fun filterList(filterStation: String){

        var tempList:MutableList<Station> = ArrayList()
        for (d in tempList){

            if(filterStation in d.name){
                tempList.add(d)

            }
        }
        stationListAdapter.updateList(tempList)
    }

    private fun getStationList(){

        progressbar.visibility = View.VISIBLE
        var apicall = ApiBixiService.create().getAllStation()

        override fun onFailure(call: Call<List<Station>>, response: Response<List<Station>>){
            progressbar.visibility = View.GONE
        }
        override fun onResponse(call:Call<List<Station>>, response:Response<List<Station>>){
            if(response.isSuccessful){
                val stationList = response.body()!!
                stationListAdapter = StationListAdapter(stationList, this)
                val mlayoutManager = LinearLayoutManager(this@ListStationActivity)
                recyclerView.layoutManager=mlayoutManager
                recyclerView.adapter
            }
        }
    }
*/