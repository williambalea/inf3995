package inf3995.test.bixiapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.station_items.view.*
import kotlin.collections.ArrayList

class StationAdapter(var clickedStationListener: ClickListener):RecyclerView.Adapter<StationAdapter.StationAdapterViewHolder>(), //var clickedItem: ClickedItem
    Filterable {


    lateinit var stationList: ArrayList<Station>
    var stationListFilter: ArrayList<Station>? = null
    lateinit var context: Context


    fun setData(stationList:ArrayList<Station>){
        this.stationList = stationList
        this.stationListFilter = stationList
        this.context = context
        notifyDataSetChanged()
    }

     class StationAdapterViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

        /* var imageView:ImageView
            var name:TextView
            var code:TextView
            var imageButton:ImageButton
            init {
                imageView = itemView.findViewById(R.id.imageView)
                name=itemView.findViewById(R.id.name)
                code=itemView.findViewById(R.id.code)
                imageButton=itemView.findViewById(R.id.buttonsOptions)
            }
        */

        var imageView = itemView.imageView
        var name = itemView.name
        var code = itemView.code
         //var imageButton = itemView.buttonsOptions

         var dataButton = itemView.data_button
         var statisticButton = itemView.statistics_button
         var predictionButton = itemView.prediction_button
         var coordinatesButton = itemView.coordinates_button

     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationAdapterViewHolder {
        return StationAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.station_items,parent,false)
        )
    }

    override fun onBindViewHolder(holder: StationAdapterViewHolder, position: Int) {
        var station = stationList[position]
        holder.name.text = station.name
        holder.code.text = station.code.toString()
        //holder.latitude.text = itemModal.latitude
        //holder.longitude.text = itemModal.longitude


        holder.coordinatesButton.setOnClickListener{
           // Toast.makeText(this, "Coordinates Button clicked", Toast.LENGTH_SHORT).show()
           val intent = Intent(context, DetailedStationActivity::class.java).putExtra("data", station)
            //startActivity(Intent(this, DetailedStationActivity::class.java).putExtra("data", station))
            context.startActivity(intent)
        }

        holder.statisticButton.setOnClickListener{
           // Toast.makeText(this, "Statistics Button clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, StatisticsStationActivity::class.java).putExtra("data", station)
            context.startActivity(intent)
        }

        holder.dataButton.setOnClickListener{
          //  Toast.makeText(this, "Data Button clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, DataStationActivity::class.java).putExtra("data", station)
            context.startActivity(intent)
        }

        holder.predictionButton.setOnClickListener{
           // Toast.makeText(this, "Prediction Button clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this.context, PredictionsStationActivity::class.java).putExtra("data", station)
            context.startActivity(intent)
        }

        holder.itemView.setOnClickListener {
               clickedStationListener.clickedStation(station)
        }

    }


    override fun getItemCount(): Int {
        return stationList.size
    }

    interface ClickListener {
       fun clickedStation(station: Station)
       //fun onClick(view: View)

    }

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(charsequence: CharSequence?): FilterResults {
                val filtersResults = FilterResults()
                if (charsequence == null || charsequence.length < 0) {
                    filtersResults.count = stationListFilter!!.size
                    filtersResults.values = stationListFilter
                } else {
                    var searchChr = charsequence.toString().toLowerCase()

                    var itemModalTemp = ArrayList<Station>()

                    for (item in stationListFilter!!) {
                        if ( item.name.toLowerCase().contains(searchChr) || item.name.toLowerCase().startsWith(searchChr) || item.code.toString().contains(searchChr)) {
                            itemModalTemp.add(item)
                        }
                    }
                    filtersResults.count = itemModalTemp.size
                    filtersResults.values = itemModalTemp
                }
                return filtersResults
            }

            override fun publishResults(constraint: CharSequence?, filtersResults: FilterResults?) {
                stationList = filtersResults!!.values as ArrayList<Station>
                notifyDataSetChanged()
            }

        }
    }

}