package com.edityomurti.openflowmanagerapp.models.flowtable.flow
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigInteger

data class Flow(
        var flowType: String? = null,
        var nodeId: String? = null,
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("cookie")
        var cookie: BigInteger? = null,
        @SerializedName("flow-name")
        var flowName: String? = null,
        @SerializedName("instructions")
        var instructions: InstructionData? = null,
        @SerializedName("flags")
        var flags: String? = null,
        @SerializedName("match")
        var match: Match? = null,
        @SerializedName("hard-timeout")
        var hardTimeOut: Int? = null,
        @SerializedName("idle-timeout")
        var idleTimeOut: Int? = null,
        @SerializedName("priority")
        var priority: Int? = null,
        @SerializedName("table_id")
        var tableId: Int? = null,
        @SerializedName("opendaylight-flow-statistics:flow-statistics")
        var flowStatistics: FlowStatistics? = null
): Serializable