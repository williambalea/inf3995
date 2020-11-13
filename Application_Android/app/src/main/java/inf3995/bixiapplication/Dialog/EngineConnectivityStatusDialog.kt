package inf3995.bixiapplication.Dialog

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
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
import kotlinx.android.synthetic.main.setting_ip_address_dialog.*
import kotlin.system.exitProcess

class EngineConnectivityStatusDialog: AppCompatDialogFragment() {

    companion object {
        lateinit var status1 :String
        lateinit var status2 :String
        lateinit var status3 :String
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
        val engineProblemNotification = ObjectAnimator.ofInt(
            animation,
            "colorFilter",
            Color.YELLOW,
            Color.WHITE
        )
        engineProblemNotification.setEvaluator(ArgbEvaluator())
        engineProblemNotification.repeatCount = ValueAnimator.INFINITE
        engineProblemNotification.repeatMode = ValueAnimator.REVERSE

        if(connectivityStatus.text == "UP")
            animation.setColorFilter(Color.GREEN)
        else
            engineProblemNotification.start()
    }

}