package com.edityomurti.openflowmanagerapp.models.flowtable.flow

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Action(
        @SerializedName("order")
        var order: Int?,
        @SerializedName("output-action")
        var outputAction: OutputAction?,
        @SerializedName("drop-action")
        var dropAction: Any?
): Serializable