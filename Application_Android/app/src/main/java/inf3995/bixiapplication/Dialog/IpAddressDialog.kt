package inf3995.bixiapplication.Dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.graphics.green
import androidx.core.graphics.toColor
import com.afollestad.vvalidator.form
import inf3995.bixiapplication.Service.WebBixiService
import inf3995.bixiapplication.UnsafeOkHttpClient
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.setting_ip_address_dialog.*
import okhttp3.internal.platform.android.AndroidSocketAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import rx.Observable
import rx.Scheduler
import rx.Subscription
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.math.log
import kotlin.system.exitProcess


const val TAG = "SettingsDialog"


class IpAddressDialog: AppCompatDialogFragment() {

    companion object {
        lateinit var ipAddressInput :String
    }

    lateinit var icon: ImageView;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: called")
        return inflater.inflate(R.layout.setting_ip_address_dialog, container, false)
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
            submitWith(okButton) { result ->
                ipAddressInput = editTextIpAddress.text.toString()
                communicationServer(ipAddressInput!!)
                connectivityCheck(ipAddressInput!!)
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
    fun connectivityCheck(ipAddress: String){

        Observable.interval(
            1, 5,
            TimeUnit.SECONDS
        )
            .observeOn(Schedulers.io())
            .subscribe {
                val retrofit4 = Retrofit.Builder()
                    .baseUrl("https://$ipAddress")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().build())
                    .build()

                val service4: WebBixiService = retrofit4.create(WebBixiService::class.java)
                val call4: Call<String> = service4.getConnectivity()

                call4.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        Log.i(TAG, "Réponse Connectivity: ${response?.body()}")
                        //R.id.connectivity.

                    }
                    override fun onFailure(call: Call<String>?, t: Throwable) {
                        Log.i(
                            TAG,
                            "Error when getting message from server!    cause: ${t.cause}     message: ${t.message}"
                        )
                    }
                })
            }


    }
}