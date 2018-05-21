package com.edityomurti.openflowmanagerapp.models.flowtable.flow

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EthernetMatch(
        @SerializedName("ethernet-type")
        var ethernetType: EthernetType?
): Serializable

class EthernetType(
        @SerializedName("type")
        var type: Int?
): Serializable