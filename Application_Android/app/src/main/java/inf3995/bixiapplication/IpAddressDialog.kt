package inf3995.bixiapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.afollestad.vvalidator.form
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.setting_ip_address_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.AccessController.getContext
import kotlin.system.exitProcess


const val TAG = "SettingsDialog"
//var connectivity: Boolean = false;
//var connectionSucced: Boolean = false

class IpAddressDialog: AppCompatDialogFragment() {

    companion object {
        lateinit var ipAddressInput :String
    }

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

        form{
            input(editTextIpAddress){
                matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\$").description("Enter a valid IP Address!")
            }
            submitWith(okButton) { result ->
                ipAddressInput = editTextIpAddress.text.toString()
                communicationServer(ipAddressInput!!)
                //Toast.makeText(activity, "Can't connect to server!", Toast.LENGTH_SHORT).show()
            }
        }
        cancelButton.setOnClickListener{ exitProcess(0);}
    }

    private fun communicationServer (ipAddress:String ){

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
                if(!response?.body().isNullOrBlank())
                    Log.i(TAG, "Réponse 1 du Serveur: ${response?.body()}")
                else
                    Log.i(TAG,"${response?.body()} --->   code:${response?.code()}    message:${response?.message()}")
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Connection status").setMessage("You have connected to the server successfully!")
                builder.show()
                dismiss()
            }
            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG,"Error when getting message from server!    cause: ${t.cause}     message: ${t.message}")
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Connection to server failed!").setMessage("cause: ${t.cause} \n message: ${t.message}")
                builder.show()
            }
        })
        /*if(connectionSucced){
            val builder = AlertDialog.Builder(this.requireContext())
            builder.setTitle("Connection status").setMessage("You have connected to the server successfully")
            builder.show()
            dismiss()
        }*/
    }
}