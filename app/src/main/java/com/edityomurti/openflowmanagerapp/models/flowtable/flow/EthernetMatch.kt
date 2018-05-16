package com.edityomurti.openflowmanagerapp.models.flowtable.flow

import com.google.gson.annotations.SerializedName

data class EthernetMatch(
        @SerializedName("ethernet-type")
        var ethernetType: EthernetType?
)

class EthernetType(
        @SerializedName("type")
        var type: Int?
)