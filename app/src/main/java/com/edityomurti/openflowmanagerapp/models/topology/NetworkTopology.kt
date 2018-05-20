package com.edityomurti.openflowmanagerapp.models.topology

import com.google.gson.annotations.SerializedName

data class NetworkTopology(
        @SerializedName("network-topology")
        var topologyData: TopologyData?
)