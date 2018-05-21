package com.edityomurti.openflowmanagerapp.models.flowtable.flow

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Match(
        @SerializedName("ethernet-match")
        var ethernetMatch: EthernetMatch?,
        @SerializedName("in-port")
        var inPort: String?
): Serializable