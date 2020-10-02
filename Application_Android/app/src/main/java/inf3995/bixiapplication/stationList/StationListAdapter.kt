package inf3995.bixiapplication.stationList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import inf3995.bixiapplication.R
import kotlinx.android.synthetic.main.item_station.view.*
import java.util.*

class StationListAdapter(private var stationListener: ClickListener
                         //private var stationListener: ListStationActivity
                        ): RecyclerView.Adapter<StationListAdapter.ViewHolder>(), View.OnClickListener, Filterable {
    lateinit var stationsFiltred: List<Station>
    public lateinit var stationsTemp: MutableList<Station>
    private lateinit var stations:List<Station>

    class ViewHolder (itemView: View):RecyclerView.ViewHolder(itemView){
        val cardview = itemView.card_view
        val name = itemView.station_name
        /*
        val cardview = itemView.findViewById<CardView>(R.id.card_view)
        val stationNameView = itemView.findViewById<TextView>(R.id.station_name)
        */
    }

    interface ClickListener {
        fun clickedStation(station:Station)
    }

    interface StationItemListener {
        fun onStationSelected(station:Station)
    }

    fun setData(stations:List<Station>){
        this.stations = stations
        this.stationsFiltred = stations
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_station, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val station = stations[position]
        holder.cardview.tag = station
        holder.name.text = station.name
        holder.itemView.setOnClickListener{
            stationListener.clickedStation(station)
            //stationListener.onStationSelected(station)
        }
       /* with(holder){
            cardview.tag = station
            stationNameView.text = station.name
            cardview.setOnClickListener(this@StationListAdapter)
            stationNameView.setOnClickListener{
                //stationListener.onStationSelected(station)
                stationListener.clickedStation(station)
            }
        }*/
    }
    override fun getItemCount(): Int {
        return stations.size
    }

   /* override fun onClick(view:View){
        when (view.id){
            R.id.card_view -> stationListener.onStationSelected(view.tag as Station)
        }
    } */

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(charsequence: CharSequence?): FilterResults {
                val filtersResults = FilterResults()
                if(charsequence == null || charsequence.length < 0) {
                    filtersResults.count = stationsFiltred.size
                    filtersResults.values = stationsFiltred
                } else {
                    var searchChr = charsequence.toString().toLowerCase(Locale.ROOT)

                    for (station in stationsFiltred){
                        if(station.name.contains(searchChr)){  // ||station.code.contains(searchChr)
                            stationsTemp.add(station)
                        }
                    }
                    filtersResults.count = stationsTemp.size
                    filtersResults.values = stationsTemp
                }
                return filtersResults
            }

            override fun publishResults(p0: CharSequence?, filtersResults: FilterResults?) {
                stations = filtersResults!!.values as List<Station>
                notifyDataSetChanged()
            }

        }
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}