package com.edityomurti.openflowmanagerapp.utils

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Path

interface Services {
    @GET("operational/opendaylight-inventory:nodes/")
    fun getTopology(): Call<ResponseBody>

    @FormUrlEncoded
    @GET("operational/opendaylight-inventory:nodes/node/{id}/table/{id}")
    fun getFlows(@Path("id") nodeId: String,
                 @Path("id") tableId: String): Call<ResponseBody>
}