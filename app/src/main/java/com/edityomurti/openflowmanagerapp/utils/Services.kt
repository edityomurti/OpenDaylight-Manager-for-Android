package com.edityomurti.openflowmanagerapp.utils

import com.edityomurti.openflowmanagerapp.models.flowtable.FlowTableData
import com.edityomurti.openflowmanagerapp.models.flowtable.flow.Flow
import com.edityomurti.openflowmanagerapp.models.flowtable.flow.FlowDataSent
import com.edityomurti.openflowmanagerapp.models.topology.NetworkTopology
import com.edityomurti.openflowmanagerapp.models.topology.Nodes
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Services {
    @GET("operational/opendaylight-inventory:nodes/")
    fun getInventoryNodes(): Call<Nodes>

    @GET("operational/opendaylight-inventory:nodes/node/{id}/table/{table_id}")
    fun getFlowsOperational(@Path("id") nodeId: String,
                 @Path("table_id") tableId: String): Call<FlowTableData>

    @GET("config/opendaylight-inventory:nodes/node/{id}/table/{table_id}")
    fun getFlowsConfig(@Path("id") nodeId: String,
                            @Path("table_id") tableId: String): Call<FlowTableData>

    @GET("operational/network-topology:network-topology")
    fun getNetworkTopology(): Call<NetworkTopology>

    @PUT("config/opendaylight-inventory:nodes/node/{node_id}/table/0/flow/{flow_id}")
    fun postFlow(@Path("node_id") node_id: String,
                 @Path("flow_id") flow_id: String,
                 @Body body: FlowDataSent): Call<ResponseBody>

    @DELETE("config/opendaylight-inventory:nodes/node/{node_id}/table/0/flow/{flow_id}")
    fun deleteFlowConfig(@Path("node_id") node_id: String,
                         @Path("flow_id") flow_id: String): Call<ResponseBody>

    @DELETE("operational/opendaylight-inventory:nodes/node/{node_id}/table/0/flow/{flow_id}")
    fun deleteFlowOperational(@Path("node_id") node_id: String,
                         @Path("flow_id") flow_id: String): Call<ResponseBody>
}