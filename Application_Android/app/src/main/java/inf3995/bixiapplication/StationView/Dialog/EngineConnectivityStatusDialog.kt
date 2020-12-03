package inf3995.bixiapplication.StationView.Dialog

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.engine_connectivity_status.*

class EngineConnectivityStatusDialog: AppCompatDialogFragment() {

    companion object {
        var status1 = "UP"
        var status2 = "UP"
        var status3 = "UP"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: called")
        return inflater.inflate(inf3995.test.bixiapplication.R.layout.engine_connectivity_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ConnectivityStatus1.text = status1
        ConnectivityStatus2.text = status2
        ConnectivityStatus3.text = status3
        imageStatusBehaviour(statusImage1,ConnectivityStatus1)
        imageStatusBehaviour(statusImage2,ConnectivityStatus2)
        imageStatusBehaviour(statusImage3,ConnectivityStatus3)
    }

    private fun imageStatusBehaviour(animation: ImageView, connectivityStatus: TextView){
        if(connectivityStatus.text == "UP")
            animation.setColorFilter(Color.GREEN)
        else
            animation.setColorFilter(Color.rgb(255,69,0))
    }
}