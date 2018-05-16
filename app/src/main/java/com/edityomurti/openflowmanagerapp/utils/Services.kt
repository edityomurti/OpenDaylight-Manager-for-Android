package com.edityomurti.openflowmanagerapp.utils

import com.edityomurti.openflowmanagerapp.models.flowtable.FlowTableData
import com.edityomurti.openflowmanagerapp.models.topology.Nodes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Services {
    @GET("operational/opendaylight-inventory:nodes/")
    fun getTopology(): Call<Nodes>

    @GET("operational/opendaylight-inventory:nodes/node/{id}/table/{table_id}")
    fun getFlows(@Path("id") nodeId: String,
                 @Path("table_id") tableId: String): Call<FlowTableData>
}