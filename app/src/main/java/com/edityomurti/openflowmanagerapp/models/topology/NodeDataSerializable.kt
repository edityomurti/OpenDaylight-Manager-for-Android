package com.edityomurti.openflowmanagerapp.models.topology

import java.io.Serializable

data class NodeDataSerializable(
        var nodeSerializableData: MutableList<NodeSerializable>
): Serializable

data class NodeSerializable(
        var nodeId: String,
        var nodeConnector: ArrayList<String>
): Serializable