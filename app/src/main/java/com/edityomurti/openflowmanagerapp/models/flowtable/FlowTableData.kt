package com.edityomurti.openflowmanagerapp.models.flowtable

import com.google.gson.annotations.SerializedName

data class FlowTableData(
        @SerializedName("flow-node-inventory:table")
        var table: MutableList<FlowTable>
)