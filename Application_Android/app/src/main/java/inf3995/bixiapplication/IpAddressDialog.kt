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
var IP_SERVER = "70.80.27.156"

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

            val ipAddressInput: String = editTextIpAddress.text.toString()
            val portInput: String = editTextPort.text.toString()

            if (ipAddressInput != "" && portInput != "") {

                communicationServer(ipAddressInput, portInput)
            } else {
                Toast.makeText(activity, "Please enter an ip address and a port!", Toast.LENGTH_SHORT).show()
            }

            dismiss()
        }

        cancelButton.setOnClickListener{dismiss()}
    }

    private fun communicationServer (ipAddress:String, port:String){

        // Post Server Ip adress
        val retrofit5 = Retrofit.Builder()
            .baseUrl("http://$ipAddress:$port/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val service5: WebBixiService = retrofit5.create(WebBixiService::class.java)
        val call5:Call<String> = service5.sendServerIP(ipAddress)

        call5.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG,"Réponse 2 du Serveur: ${response?.body()}")
            }
            override fun onFailure(call: Call<String>?, t: Throwable) {
            }
        })

        // Get Hello World
        val retrofit4 = Retrofit.Builder()
            .baseUrl("http://$ipAddress:$port/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val service4: WebBixiService = retrofit4.create(WebBixiService::class.java)
        val call4: Call<String> = service4.getHelloWorld()

        call4.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG, "Réponse 1 du Serveur: ${response?.body()}")
            }

            override fun onFailure(call: Call<String>?, t: Throwable) {
            }
        })
    }
}