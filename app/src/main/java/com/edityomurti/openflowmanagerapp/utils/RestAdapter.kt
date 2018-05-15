package com.edityomurti.openflowmanagerapp.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestAdapter() {
    val BASE_URL = "192.168.56.1:8181/restconf/"
    val USERNAME = "admin"
    val PASSWORD = "admin"

    var endpoint: Services

    init {
        var client = OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor(USERNAME, PASSWORD))
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()


        var retrofit = Retrofit.Builder()
                .baseUrl("http://$BASE_URL")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        endpoint = retrofit.create(Services::class.java)
    }

    fun getEndPoint(): Services{
        return endpoint
    }

}