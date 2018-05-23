package com.edityomurti.openflowmanagerapp.models.flowtable.flow
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigInteger

data class Flow(
        var flowType: String?,
        var nodeId: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("cookie")
        var cookie: BigInteger?,
        @SerializedName("flow-name")
        var flowName: String?,
        @SerializedName("instructions")
        var instructions: InstructionData?,
        @SerializedName("flags")
        var flags: String?,
        @SerializedName("match")
        var match: Match?,
        @SerializedName("hard-timeout")
        var hardTimeOut: Int?,
        @SerializedName("idle-timeout")
        var idleTimeOut: Int?,
        @SerializedName("priority")
        var priority: Int?,
        @SerializedName("table_id")
        var tableId: Int?,
        @SerializedName("opendaylight-flow-statistics:flow-statistics")
        var flowStatistics: FlowStatistics?
): Serializable