package inf3995.test.bixiapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.station_items.*
import kotlinx.android.synthetic.main.station_items_with_button.view.*
import kotlinx.android.synthetic.main.station_items_with_button.view.code
import kotlinx.android.synthetic.main.station_items_with_button.view.imageView
import kotlinx.android.synthetic.main.station_items_with_button.view.name
import kotlinx.android.synthetic.main.station_items.view.*
import kotlin.collections.ArrayList

class StationAdapter(var clickedStationListener: ClickListener):RecyclerView.Adapter<StationAdapter.StationAdapterViewHolder>(), //var clickedItem: ClickedItem
    Filterable {


    lateinit var stationList: ArrayList<Station>
    var stationListFilter: ArrayList<Station>? = null
    lateinit var context: Context


    fun setData(stationList:ArrayList<Station>, context: Context){
        this.stationList = stationList
        this.stationListFilter = stationList
        this.context = context
        notifyDataSetChanged()
    }

     class StationAdapterViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {



        var imageView = itemView.imageView
        var name = itemView.name
        var code = itemView.code
         //var imageButton = itemView.buttonsOptions

         var dataButton = itemView.data_button
         var statisticButton = itemView.statistics_button
         var predictionButton = itemView.prediction_button
         var coordinatesButton = itemView.coordinates_button
         var dropDownMenu = itemView.dropDownMenu
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

        holder.dropDownMenu.setOnClickListener{
            //var popupMenu = PopupMenu(this.context,holder.dropDownMenu)
            val wrapper = ContextThemeWrapper(this.context, R.style.BasePopupMenu)
             val popupMenu = android.widget.PopupMenu(wrapper, holder.dropDownMenu)

            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.action_popup_details ->
                        context.startActivity(Intent(context, StationCoordinatesActivity::class.java).putExtra("data", station))
                    R.id.action_popup_data ->
                        context.startActivity(Intent(context, StationsDataActivity::class.java).putExtra("data", station))
                    R.id.action_popup_statistics ->
                        context.startActivity(Intent(context, StationStatisticsActivity::class.java).putExtra("data", station))
                    R.id.action_popup_predictions ->
                        context.startActivity(Intent(context, StationPredictionsActivity::class.java).putExtra("data", station))
                }
                true
            })
            popupMenu.show()
        }

    }


    override fun getItemCount(): Int {
        return stationList.size
    }

    interface ClickListener {
       fun clickedStation(station: Station)

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