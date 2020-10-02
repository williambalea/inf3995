package inf3995.bixiapplication.stationList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import inf3995.bixiapplication.R

class StationFragment: Fragment() {
    private lateinit var stations:MutableList<Station>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StationListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_station,container, false)
        recyclerView = view.findViewById(R.id.stations_recycler_view)
        recyclerView.layoutManager =LinearLayoutManager(context)
                return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // adapter = StationAdapter(stations, this)
     //   recyclerView.adapter = adapter
    }


}

