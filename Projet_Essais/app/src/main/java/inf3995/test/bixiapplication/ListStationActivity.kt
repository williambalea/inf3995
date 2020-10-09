package inf3995.test.bixiapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.station_items.*
import kotlinx.android.synthetic.main.station_list_activity_main.*

class ListStationActivity : AppCompatActivity(), StationAdapter.ClickedItem {

    var itemListModal = arrayOf(
        Station(123, "Cote vertu Est", 11.244456F, 21.076599F),
        Station(452, "Langelier Est", 12.374400F, 22.356466F),
        Station(375, "Montreal Nord", 13.365400F, 23.000466F),
        Station(458, "Polytechnique", 14.321400F, 24.348466F),
        Station(589, "Montréal", 15.374980F, 25.356466F),
        Station(600, "Boulevard Saint Laurent", 16.3744180F, 26.986466F),
        Station(987, "Saint michel", 17.365400F, 27.000466F),
        Station(876, "Cote des neiges", 18.321400F, 28.348466F),
        Station(239, "Centre ville", 19.374980F, 29.356466F),
        Station(120, "Avenue papineau", 20.3744180F, 30.986466F)
    )
    var itemModalList = ArrayList<Station>()
    var stationAdapter: StationAdapter? = null
  //  var popupMenu:PopupMenu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.station_list_activity_main)

        for(item in itemListModal){
            itemModalList.add(item)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        stationAdapter = StationAdapter(this)
        stationAdapter!!.setData(itemModalList)
        recyclerView.adapter = stationAdapter

       // popupMenu = PopupMenu.findViewById(R.id.buttonsOptions)
    /*
        val popupMenu = PopupMenu(this,findViewById(R.id.buttonsOptions) )
        popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
                item ->
            when(item.itemId){
                R.id.action_popup_details ->{
                    val intent = Intent(this, DetailedStationActivity::class.java).putExtra("data",itemModal)
                    startActivity(intent)
                    true
                }
                R.id.action_popup_data ->{
                    val intent = Intent(this, DataStationActivity::class.java).putExtra("data",itemModal)
                    startActivity(intent)
                    true
                }
                R.id.action_popup_statistics ->{
                    val intent = Intent(this, StatisticsStationActivity::class.java).putExtra("data",itemModal)
                    startActivity(intent)
                    true
                }
                R.id.action_popup_predictions ->{
                    val intent = Intent(this, PredictionsStationActivity::class.java).putExtra("data",itemModal)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        //popupMenu.inflate(R.menu.popup_menu)
        buttonsOptions.setOnClickListener{
            popupMenu.show()
        }
    */

    }

    override fun clickedItem(station: Station) {
        var itemModal = station
        Log.e("TAG","===> " + itemModal.name)
        var name = itemModal.name
        startActivity(Intent(this@ListStationActivity, DetailedStationActivity::class.java).putExtra("data", itemModal))

        /*
        val popupMenu = PopupMenu(this,findViewById(R.id.buttonsOptions) )
        popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
      //  menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

        when(popupMenu.){
            R.id.action_popup_details ->{
                val intent = Intent(this, DetailedStationActivity::class.java).putExtra("data", itemModal)
                startActivity(intent)
                true
            }
            R.id.action_popup_data ->{
                val intent = Intent(this, DataStationActivity::class.java).putExtra("data",itemModal)
                startActivity(intent)
                true
            }
            R.id.action_popup_statistics ->{
                val intent = Intent(this, StatisticsStationActivity::class.java).putExtra("data",itemModal)
                startActivity(intent)
                true
            }
            R.id.action_popup_predictions ->{
                val intent = Intent(this, PredictionsStationActivity::class.java).putExtra("data",itemModal)
                startActivity(intent)
                true
            }
        */
    }


    /*
    override fun clickOptionsItems(station: Station) {
        var itemModal = station
        val popupMenu = PopupMenu(this, buttonsOptions)
        popupMenu.setOnMenuItemClickListener {
                item ->
            when(item.itemId){
                R.id.action_popup_details ->{
                    val intent = Intent(this, DetailedStationActivity::class.java).putExtra("data",itemModal)
                    startActivity(intent)
                    true
                }
                R.id.action_popup_data ->{
                    val intent = Intent(this, DataStationActivity::class.java).putExtra("data",itemModal)
                    startActivity(intent)
                    true
                }
                R.id.action_popup_statistics ->{
                    val intent = Intent(this, StatisticsStationActivity::class.java).putExtra("data",itemModal)
                    startActivity(intent)
                    true
                }
                R.id.action_popup_predictions ->{
                    val intent = Intent(this, PredictionsStationActivity::class.java).putExtra("data",itemModal)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.show()
    }
*/
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



}