package com.edityomurti.openflowmanagerapp.models.topology

import com.google.gson.annotations.SerializedName

data class NodeConnector(
        @SerializedName("id")
        var id: String?,
        @SerializedName("flow-node-inventory:hardware-address")
        var hardwareAddress: String?,
        @SerializedName("flow-node-inventory:name")
        var name: String?,
        @SerializedName("address-tracker:addresses")
        var adresses: MutableList<AddressTrackerAdresses>?
)