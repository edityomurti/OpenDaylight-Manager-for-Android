package com.edityomurti.openflowmanagerapp.models.flowtable.flow

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FlowStatistics(
        @SerializedName("packet-count")
        var packetCount: Int?,
        @SerializedName("byte-count")
        var byteCount: Int?,
        @SerializedName("duration")
        var duration: Duration
): Serializable