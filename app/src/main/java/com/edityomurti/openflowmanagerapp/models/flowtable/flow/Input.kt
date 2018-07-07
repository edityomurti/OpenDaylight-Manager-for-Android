package com.edityomurti.openflowmanagerapp.models.flowtable.flow

import com.google.gson.annotations.SerializedName

data class Input(
        @SerializedName("match")
        var match: Match?,
        @SerializedName("table_id")
        var tableId: Int?,
        @SerializedName("priority")
        var priority: Int?,
        @SerializedName("node")
        var node: String?
)