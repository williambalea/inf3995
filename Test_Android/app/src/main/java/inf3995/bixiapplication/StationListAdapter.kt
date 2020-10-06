package inf3995.bixiapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_station.view.*
import java.util.*

class StationListAdapter(var clickListener: StationClickListener) : RecyclerView.Adapter<StationListAdapter.StationViewHolder>(), Filterable {          //View.OnClickListener,

    private lateinit var stationsListFiltered: ArrayList<Station>
    private lateinit var stations: ArrayList<Station>
    private lateinit var listStationActivity: StationListAdapter

    fun setData(stations: ArrayList<Station>) {
        this.stations = stations
        this.stationsListFiltered = stations

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_station, parent, false)
        return StationViewHolder(viewItem)
    }

    class StationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.station_name
        val code = itemView.station_code
        val image = itemView.imageView
    }

    interface StationClickListener {
        fun onStationClicked(station: Station)
    }

    override fun getItemCount(): Int {
        return stations.size
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val station = stations[position]
        holder.name.text = station.name
        holder.code.text = station.code.toString()
        // holder.image.setImageResource(station.image)
        holder.itemView.setOnClickListener {
            clickListener.onStationClicked(station)

        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charsequence: CharSequence?): FilterResults {
                val filtersResults = FilterResults()
                if (charsequence == null || charsequence.length < 0) {
                    filtersResults.count = stationsListFiltered.size
                    filtersResults.values = stationsListFiltered
                } else {
                    var searchChr = charsequence.toString().toLowerCase()

                    var stationsTemp = ArrayList<Station>()

                    for (station in stationsListFiltered) {
                        if (station.name.contains(searchChr) || station.code.toString()
                                .contains(searchChr)
                        ) {
                            stationsTemp.add(station)
                        }
                    }
                    filtersResults.count = stationsTemp.size
                    filtersResults.values = stationsTemp
                }
                return filtersResults
            }

            override fun publishResults(p0: CharSequence?, filtersResults: FilterResults?) {
                stations = filtersResults!!.values as ArrayList<Station>
                notifyDataSetChanged()
            }

        }


    }
}




/*
    interface StationClickListener {
        fun clickedStation(station:Station)
    }
      /*
    fun updateList (list:MutableList<Station>){
        stationList = list
        notifyDataSetChanged()
    }*/

    fun setData(stations:ArrayList<Station>){
        this.stations = stations
        this.stationsFiltred = stations
        this.stationListener = stationListener
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_station, parent, false)
        return StationViewHolder(viewItem)
    }


    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val station = stations[position]
        holder.name.text = station.name
        holder.code.text = station.code.toString()
        holder.itemView.setOnClickListener{
            stationListener.clickedStation(station)

        }
    }



    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(charsequence: CharSequence?): FilterResults {
                val filtersResults = FilterResults()
                if(charsequence == null || charsequence.length < 0) {
                    filtersResults.count = stationsFiltred.size
                    filtersResults.values = stationsFiltred
                } else {
                    var searchChr = charsequence.toString().toLowerCase()

                    val stationsTemp =ArrayList<Station>()

                    for (station in stationsFiltred){
                        if(station.name.contains(searchChr)||station.code.toString().contains(searchChr)){
                            stationsTemp.add(station)
                        }
                    }
                    filtersResults.count = stationsTemp.size
                    filtersResults.values = stationsTemp
                }
                return filtersResults
            }

            override fun publishResults(p0: CharSequence?, filtersResults: FilterResults?) {
                stations = filtersResults!!.values as ArrayList<Station>
                notifyDataSetChanged()
            }

        }
    }

}*/
