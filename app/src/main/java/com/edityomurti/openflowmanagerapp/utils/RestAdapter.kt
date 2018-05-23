package com.edityomurti.openflowmanagerapp.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestAdapter(var context: Context) {
    lateinit var sharedPreferences: SharedPreferences

    var CONTROLLER_IP_ADDRESS: String? = null
    var PORT_NUMBER: String? = null
    var BASE_URL: String? = null
    var USERNAME: String? = null
    var PASSWORD: String? = null

    var endpoint: Services

    init {
        sharedPreferences = context.getSharedPreferences(Constants.DEFAULT_PREFS_NAME, Activity.MODE_PRIVATE)

        CONTROLLER_IP_ADDRESS = sharedPreferences.getString(Constants.CONTROLLER_IP_ADDRESS, null)
        PORT_NUMBER = sharedPreferences.getString(Constants.CONTROLLER_PORT_ADDRESS, null)
        USERNAME = sharedPreferences.getString(Constants.CONTROLLER_USERNAME, null)
        PASSWORD = sharedPreferences.getString(Constants.CONTROLLER_PASSWORD, null)

        if(CONTROLLER_IP_ADDRESS == null){
            CONTROLLER_IP_ADDRESS = "192.168.56.1"
        }

        if(PORT_NUMBER == null){
            PORT_NUMBER = "6633"
        }

        BASE_URL = "$CONTROLLER_IP_ADDRESS:$PORT_NUMBER"

        if(USERNAME == null){
            USERNAME = "admin"
        }

        if(PASSWORD == null){
            PASSWORD = "admin"
        }

        var client = OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor(USERNAME!!, PASSWORD!!))
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()


        var retrofit = Retrofit.Builder()
                .baseUrl("http://$BASE_URL/restconf/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        endpoint = retrofit.create(Services::class.java)
    }

    fun getEndPoint(): Services{
        return endpoint
    }

}