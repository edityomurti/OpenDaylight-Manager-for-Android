package com.edityomurti.openflowmanagerapp.models.topology

import com.google.gson.annotations.SerializedName

data class Nodes(
        @SerializedName("nodes")
        var nodeData: NodeData
)