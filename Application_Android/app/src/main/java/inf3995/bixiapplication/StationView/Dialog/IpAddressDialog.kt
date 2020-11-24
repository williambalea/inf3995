package inf3995.bixiapplication.StationView.Dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.afollestad.vvalidator.form
import inf3995.bixiapplication.StationViewModel.WebBixiService
import kotlinx.android.synthetic.main.setting_ip_address_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.system.exitProcess


const val TAG = "SettingsDialog"


class IpAddressDialog: AppCompatDialogFragment() {

    companion object {
        var ipAddressInput :String = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: called")
        return inflater.inflate(inf3995.test.bixiapplication.R.layout.setting_ip_address_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: called")
        super.onViewCreated(view, savedInstanceState)

        form{
            input(editTextIpAddress){
                matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\$").description(
                    "Enter a valid IP Address!"
                )
            }
            submitWith(okButton) {
                ipAddressInput = editTextIpAddress.text.toString()
                communicationServer(ipAddressInput)
                //connectivityCheck(ipAddressInput)
            }
        }
        cancelButton.setOnClickListener{ exitProcess(0);}
    }

    private fun communicationServer(ipAddress: String){

        // Get Hello World
        val retrofit4 = Retrofit.Builder()
            .baseUrl("https://$ipAddress")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().build())
            .build()

        val service4: WebBixiService = retrofit4.create(WebBixiService::class.java)
        val call4: Call<String> = service4.getHelloWorld()

        call4.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                if (!response?.body().isNullOrBlank())
                    Log.i(TAG, "Réponse 1 du Serveur: ${response?.body()}")
                else
                    Log.i(
                        TAG,
                        "${response?.body()} --->   code:${response?.code()}    message:${response?.message()}"
                    )
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Connection Successful!")
                    .setMessage("You have connected to the server successfully!")
                builder.show()
                dismiss()
            }

            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(
                    TAG,
                    "Error when getting message from server!    cause: ${t.cause}     message: ${t.message}"
                )
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Connection to server failed!")
                    .setMessage("cause: ${t.cause} \n message: ${t.message}")
                builder.show()
            }
        })

    }
}