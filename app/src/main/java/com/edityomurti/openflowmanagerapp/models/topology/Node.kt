package com.edityomurti.openflowmanagerapp.models.topology

import com.google.gson.annotations.SerializedName

data class Node(
        @SerializedName("node-id")
        var nodeId: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("flow-node-inventory:ip-address")
        var ipAddress: String?,
        @SerializedName("node-connector")
        var nodeConnector: MutableList<NodeConnector>?,
        @SerializedName("termination-point")
        var terminationPointData: MutableList<TerminationPoint>,
        @SerializedName("host-tracker-service:attachment-points")
        var hostTrackerAttachmentPointData: MutableList<HostTrackerAttachmentPoint>,
        @SerializedName("host-tracker-service:addresses")
        var hostTrackerAddressesData: MutableList<HostTrackerAddress>,
        @SerializedName("host-tracker-service:id")
        var hostTrackerId: String?

)

data class TerminationPoint(
        @SerializedName("tp-id")
        var tpId: String?
)

data class HostTrackerAttachmentPoint(
        @SerializedName("tp-id")
        var tpId: String?,
        @SerializedName("corresponding-tp")
        var correspondingTp: String?,
        @SerializedName("active")
        var active: Boolean?
)

data class HostTrackerAddress(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("mac")
        var mac: String?,
        @SerializedName("ip")
        var ip: String?

)