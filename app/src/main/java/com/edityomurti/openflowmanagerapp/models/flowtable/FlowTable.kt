package com.edityomurti.openflowmanagerapp.models.flowtable

import com.edityomurti.openflowmanagerapp.models.flowtable.flow.Flow
import com.google.gson.annotations.SerializedName

data class FlowTable(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("flow-hash-id-map")
        var flowHashIdMapData: MutableList<FlowHashIdMap>?,
        @SerializedName("flow")
        var flowData: MutableList<Flow>?
)