package com.edityomurti.openflowmanagerapp.models.flowtable.flow

import com.google.gson.annotations.SerializedName

data class Action(
        @SerializedName("order")
        var order: Int?,
        @SerializedName("output-action")
        var outputAction: OutputAction?
)