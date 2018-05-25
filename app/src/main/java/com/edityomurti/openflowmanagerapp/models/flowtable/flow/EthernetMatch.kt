package com.edityomurti.openflowmanagerapp.models.flowtable.flow

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EthernetMatch(
        @SerializedName("ethernet-source")
        var ethernetSource: EthernetSource?,
        @SerializedName("ethernet-destination")
        var ethernetDestination: EthernetDestination?,
        @SerializedName("ethernet-type")
        var ethernetType: EthernetType?
): Serializable

class EthernetType(
        @SerializedName("type")
        var type: Int?
): Serializable

class EthernetSource(
        @SerializedName("address")
        var address: String?
): Serializable

class EthernetDestination(
        @SerializedName("address")
        var address: String?
): Serializable