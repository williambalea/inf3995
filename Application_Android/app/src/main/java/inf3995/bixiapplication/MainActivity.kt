package inf3995.bixiapplication

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    //var URL_BASE = "http://70.52.15.59:2001"
    var URL_BASE = "http://70.80.27.156:2000"
    var IP_SERVER = "70.80.27.156"
    val dialog = IpAddressDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialog.show(supportFragmentManager,null)
        // STRING
        val retrofit = Retrofit.Builder()
            .baseUrl("http://httpbin.org")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val service:HttpWebServicesString = retrofit.create(HttpWebServicesString::class.java)
        val call:Call<String> = service.getUserAgent()
        val call2:Call<String> = service.getUserIP()
        call.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG,"User agent response: ${response?.body()}")
            }
            override fun onFailure(call: Call<String>?, t: Throwable) {
            }
        })

       call2.enqueue(object: Callback<String> {
           override fun onResponse(call2: Call<String>?, response: Response<String>?) {
                Log.i(TAG,"User IP response: ${response?.body()}")
           }
           override fun onFailure(call2: Call<String>, t: Throwable) {
           }
        })

        // JSON
        val retrofitJson = Retrofit.Builder()
            .baseUrl("http://httpbin.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val serviceJson = retrofitJson.create(HttpWebServicesJson::class.java)
        val callJson = serviceJson.getUserInfo()
        callJson.enqueue(object:Callback<GetData> {

            override fun onResponse(call: Call<GetData>?, response: Response<GetData>?) {
                val getData= response?.body()
                Log.i(TAG, "Received url: ${getData}")
            }
            override fun onFailure(call: Call<GetData>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })

        // Get Hello World
        val retrofit4 = Retrofit.Builder()
            .baseUrl(URL_BASE +"/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val service4:WebBixiService = retrofit4.create(WebBixiService::class.java)
        val call4:Call<String> = service4.getHelloWorld()

        call4.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG,"Réponse 1 du Serveur: ${response?.body()}")
            }
            override fun onFailure(call: Call<String>?, t: Throwable) {
            }
        })

        // Post Server Ip adress

<<<<<<< HEAD
        val retrofit5 = Retrofit.Builder()
            .baseUrl(URL_BASE +"/")
=======
        // Test

    }

    override fun onPause() {
        super.onPause()

    }
    override fun onResume() {
        super.onResume()
        communicationServer(urlBase)
    }

    private fun communicationServer (ipAddress:String){
        val retrofit3 = Retrofit.Builder()
            .baseUrl("http://$ipAddress/")
>>>>>>> 07a8017... [Robin] send data from diaglod to activity(not completed)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val service5:WebBixiService = retrofit5.create(WebBixiService::class.java)
        val call5:Call<String> = service5.sendServerIP(IP_SERVER)

        call5.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG,"Réponse 2 du Serveur: ${response?.body()}")
            }
            override fun onFailure(call: Call<String>?, t: Throwable) {
            }
        })

    }

}