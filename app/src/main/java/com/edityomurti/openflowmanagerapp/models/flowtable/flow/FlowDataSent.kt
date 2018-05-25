package com.edityomurti.openflowmanagerapp.models.flowtable.flow

import com.google.gson.annotations.SerializedName

data class FlowDataSent(
        @SerializedName("flow")
        var flowData: MutableList<Flow>?
)