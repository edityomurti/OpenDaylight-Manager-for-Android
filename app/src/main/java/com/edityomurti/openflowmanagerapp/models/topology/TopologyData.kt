package com.edityomurti.openflowmanagerapp.models.topology

import com.google.gson.annotations.SerializedName

data class TopologyData(
        @SerializedName("topology")
        var topology: MutableList<Topology>
)