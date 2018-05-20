package com.edityomurti.openflowmanagerapp.models.topology

import com.google.gson.annotations.SerializedName

data class Topology(
        @SerializedName("topology-id")
        var topologyName: String?,
        @SerializedName("node")
        var nodeData: MutableList<Node>?,
        @SerializedName("link")
        var linkData: MutableList<Link>?
)