package inf3995.bixiapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.setting_ip_address_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


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

            val input: String = editTextIpAddress.text.toString()

            if (input != "") {

                communicationServer(input)
            } else {
                Toast.makeText(activity, "Please a ip address!", Toast.LENGTH_SHORT).show()
            }

            dismiss()
        }


        cancelButton.setOnClickListener{dismiss()}
    }

    private fun communicationServer (ipAddress:String){
        val retrofit3 = Retrofit.Builder()
            .baseUrl("http://$ipAddress/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val service1:WebBixiService = retrofit3.create(WebBixiService::class.java)
        val call3: Call<String> = service1.getTest()

        call3.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG,"Test du Serveur: ${response?.body()}")
            }
            override fun onFailure(call: Call<String>?, t: Throwable) {
            }
        })
    }
}