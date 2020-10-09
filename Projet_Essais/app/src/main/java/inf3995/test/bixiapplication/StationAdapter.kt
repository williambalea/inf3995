package inf3995.test.bixiapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.station_items.view.*
import kotlin.collections.ArrayList

class StationAdapter(var clickedItem: ClickedItem):RecyclerView.Adapter<StationAdapter.StationAdapterViewHolder>(),
    Filterable {

    lateinit var stationList: ArrayList<Station>
    lateinit var stationListFilter: ArrayList<Station>


    fun setData(stationList:ArrayList<Station>){
        this.stationList = stationList
        this.stationListFilter = stationList
        notifyDataSetChanged()
    }

    class StationAdapterViewHolder(itemView:View):RecyclerView.ViewHolder(itemView),View.OnClickListener {
        var imageView = itemView.imageView
        var name = itemView.name
        var code = itemView.code
        var imageButton = itemView.buttonsOptions
        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }

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
        holder.itemView.setOnClickListener{
            clickedItem.clickedItem(station)
        }

    /*
        holder.imageButton.setOnClickListener{
            val popupMenu = PopupMenu(context, holder.imageButton,GravityCompat.START )
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {item ->
                when(item.itemId){
                    R.id.action_popup_details ->{
                        Toast.makeText(context,context.getString(R.string.station_coordinates), Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_popup_data ->{
                        Toast.makeText(context,context.getString(R.string.station_data), Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_popup_statistics ->{
                        Toast.makeText(context,context.getString(R.string.station_statistics), Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_popup_predictions ->{
                        Toast.makeText(context,context.getString(R.string.station_predictions), Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
   */
        /*when(popupMenu.){
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

    override fun getItemCount(): Int {
        return stationList.size
    }

    interface ClickedItem {
       fun clickedItem(station: Station)
        //fun clickOptionsItems(station:Station)
    }

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(charsequence: CharSequence?): FilterResults {
                val filtersResults = FilterResults()
                if (charsequence == null || charsequence.length < 0) {
                    filtersResults.count = stationListFilter.size
                    filtersResults.values = stationListFilter
                } else {
                    var searchChr = charsequence.toString().toLowerCase()

                    var itemModalTemp = ArrayList<Station>()

                    for (item in stationListFilter) {
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