package com.edityomurti.openflowmanagerapp.models.topology

import com.google.gson.annotations.SerializedName

data class Node(
        @SerializedName("id")
        var id: String?,
        @SerializedName("flow-node-inventory:ip-address")
        var ipAddress: String?,
        @SerializedName("node-connector")
        var nodeConnector: MutableList<NodeConnector>?
)