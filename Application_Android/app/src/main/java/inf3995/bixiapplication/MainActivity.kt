package inf3995.bixiapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
    }

}