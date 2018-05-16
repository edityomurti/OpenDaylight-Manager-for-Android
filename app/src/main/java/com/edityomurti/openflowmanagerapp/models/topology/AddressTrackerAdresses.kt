package com.edityomurti.openflowmanagerapp.models.topology

import com.google.gson.annotations.SerializedName

data class AddressTrackerAdresses(
        @SerializedName("id")
        var id: String?,
        @SerializedName("mac")
        var mac: String?,
        @SerializedName("ip")
        var ip: String?
)