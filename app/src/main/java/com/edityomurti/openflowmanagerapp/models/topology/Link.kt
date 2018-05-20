package com.edityomurti.openflowmanagerapp.models.topology

import com.google.gson.annotations.SerializedName

data class Link(
        @SerializedName("ink-id")
        var linkId: String?,
        @SerializedName("source")
        var source: Source?,
        @SerializedName("destination")
        var destination: Destination?
)

data class Source(
        @SerializedName("source-tp")
        var sourceTp: String?,
        @SerializedName("source-node")
        var sourceNode: String?
)

data class Destination(
        @SerializedName("dest-node")
        var destNode: String?,
        @SerializedName("dest-tp")
        var destTp: String?
)