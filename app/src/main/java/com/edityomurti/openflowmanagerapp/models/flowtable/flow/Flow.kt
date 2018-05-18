package com.edityomurti.openflowmanagerapp.models.flowtable.flow

import com.google.gson.annotations.SerializedName

data class Flow(
        var nodeId: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("instruction")
        var instructions: InstructionData?,
        @SerializedName("flags")
        var flags: String?,
        @SerializedName("match")
        var match: Match?,
        @SerializedName("priority")
        var priority: Int?,
        @SerializedName("table_id")
        var tableId: Int?
)