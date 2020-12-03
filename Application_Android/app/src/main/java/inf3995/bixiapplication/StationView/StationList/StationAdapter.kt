package inf3995.bixiapplication.StationView.StationList

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.RecyclerView
import inf3995.bixiapplication.StationView.MainScreen.MainScreenActivity
import inf3995.bixiapplication.StationView.Predictions.StationPredictions.StationPredictionsActivity
import inf3995.bixiapplication.StationView.Statistics.StationStatistics.StationStatisticsActivity
import inf3995.bixiapplication.StationViewModel.StationLiveData.Station
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.station_items.view.*


class StationAdapter():RecyclerView.Adapter<StationAdapter.StationAdapterViewHolder>(),
    Filterable {
    lateinit var stationList: ArrayList<Station>
    var stationListFilter: ArrayList<Station>? = null
    lateinit var context: Context

     fun setData(stationList: ArrayList<Station>, context: Context){
        this.stationList = stationList
        this.stationListFilter = stationList
        this.context = context
        notifyDataSetChanged()
    }

     class StationAdapterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.name
        var code: TextView = itemView.code
         var dropDownMenu: ImageButton = itemView.dropDownMenu
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationAdapterViewHolder {
        return StationAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.station_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StationAdapterViewHolder, position: Int) {
        val station = stationList[position]
        holder.name.text = station.name
        holder.code.text = station.code.toString()

        holder.dropDownMenu.setOnClickListener{
            val wrapper = ContextThemeWrapper(this.context, R.style.BasePopupMenu)
            val popupMenu = PopupMenu(wrapper, holder.dropDownMenu)

            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            if(MainScreenActivity.connectivity.value?.get(1) == "DOWN") {
                popupMenu.menu.getItem(1).isVisible = false
            }

            if(MainScreenActivity.connectivity.value?.get(2) == "DOWN") {
                popupMenu.menu.getItem(2).isVisible = false
            }

            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_popup_details ->
                        context.startActivity(
                            Intent(
                                context,
                                StationCoordinatesActivity::class.java
                            ).putExtra("data", station)
                        )
                    R.id.action_popup_statistics ->
                        context.startActivity(
                            Intent(context, StationStatisticsActivity::class.java).putExtra(
                                "data",
                                station
                            )
                        )
                    R.id.action_popup_predictions ->
                        context.startActivity(
                            Intent(
                                context,
                                StationPredictionsActivity::class.java
                            ).putExtra("data", station)
                        )
                }
                true
            })
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return stationList.size
    }

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(charsequence: CharSequence?): FilterResults {
                val filtersResults = FilterResults()
                if (charsequence == null || charsequence.length < 0) {
                    filtersResults.count = stationListFilter!!.size
                    filtersResults.values = stationListFilter
                } else {
                    val searchChr = charsequence.toString().toLowerCase()

                    val itemModalTemp = ArrayList<Station>()

                    for (item in stationListFilter!!) {
                        if ( item.name.toLowerCase().contains(searchChr) || item.name.toLowerCase().startsWith(
                                searchChr
                            ) || item.code.toString().contains(searchChr)) {
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