package com.edityomurti.openflowmanagerapp.models.topology

data class Device(
        var deviceName: String?,
        var deviceDesc: String?,
        var deviceType: String?,
        var linkDevice: MutableList<Link>
)