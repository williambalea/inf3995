package inf3995.bixiapplication.station

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import inf3995.bixiapplication.R

class StationAdapter(private val stations:List<Station>,
                     private val stationListener: StationFragment
)
                    : RecyclerView.Adapter<StationAdapter.ViewHolder>(), View.OnClickListener {
    interface StationItemListener {
        fun onStationSelected(station:Station)
    }

    class ViewHolder (itemView: View):RecyclerView.ViewHolder(itemView){
        val cardview = itemView.findViewById<CardView>(R.id.card_view)
        val stationNameView = itemView.findViewById<TextView>(R.id.name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_station, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val station = stations[position]
        with(holder){
            cardview.tag = station
            cardview.setOnClickListener(this@StationAdapter)
            stationNameView.text = station.name
        }
    }
    override fun getItemCount(): Int = stations.size

    override fun onClick(view:View){
        when (view.id){
            R.id.card_view -> stationListener.onStationSelected(view.tag as Station)
        }
    }
}