package com.edityomurti.openflowmanagerapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.edityomurti.openflowmanagerapp.utils.RestAdapter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    val TAG = "HomeActivity"

    lateinit var restAdapter: RestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        restAdapter = RestAdapter()

        restAdapter.getEndPoint().getTopology().enqueue(object : retrofit2.Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                if(response?.isSuccessful!!){
                    println("$TAG , isSuccessful")
                    println("$TAG , responseBody: ${response.body()}")
                } else {
                    println("$TAG , isNOTSuccessful")
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                println("$TAG , onFailure")
            }
        })

    }
}