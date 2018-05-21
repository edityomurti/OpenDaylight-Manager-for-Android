package com.edityomurti.openflowmanagerapp.models.flowtable.flow

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Duration(
        @SerializedName("nanosecond")
        var nanoSecond: Int?,
        @SerializedName("second")
        var second: Int?
): Serializable