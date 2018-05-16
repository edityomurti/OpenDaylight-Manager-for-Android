package com.edityomurti.openflowmanagerapp.models.flowtable

import com.google.gson.annotations.SerializedName

data class FlowHashIdMap(
        @SerializedName("hash")
        var hash: String?,
        @SerializedName("flow-id")
        var flowId: String?
)