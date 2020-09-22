package inf3995.bixiapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.setting_ip_address_dialog.*

private const val TAG = "SettingsDialog"

class IpAddressDialog: AppCompatDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"onCreateView: called")
        return inflater.inflate(R.layout.setting_ip_address_dialog,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG,"onViewCreated: called")
        super.onViewCreated(view, savedInstanceState)
        okButton.setOnClickListener{
            //saveValues
            dismiss()
        }

        cancelButton.setOnClickListener{dismiss()}
    }
}