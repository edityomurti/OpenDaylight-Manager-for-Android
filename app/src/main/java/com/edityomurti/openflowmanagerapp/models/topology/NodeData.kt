package com.edityomurti.openflowmanagerapp.models.topology

import com.google.gson.annotations.SerializedName

data class NodeData(
        @SerializedName("node")
        var node: MutableList<Node>
)