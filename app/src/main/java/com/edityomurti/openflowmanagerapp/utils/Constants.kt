package com.edityomurti.openflowmanagerapp.utils

class Constants{
    companion object {
        val DEFAULT_PREFS_NAME = "DEFAULT_PREFS_NAME"
        val CONTROLLER_IP_ADDRESS = "CONTROLLER_IP_ADDRESS"
        val CONTROLLER_PORT_ADDRESS = "CONTROLLER_PORT_ADDRESS"
        val CONTROLLER_USERNAME = "CONTROLLER_USERNAME"
        val CONTROLLER_PASSWORD = "CONTROLLER_PASSWORD"
        val DEVICE_TYPE_SWITCH = "DEVICE_TYPE_SWITCH"
        val DEVICE_TYPE_HOST = "DEVICE_TYPE_HOST"
        val OBJECT_FLOW = "OBJECT_FLOW"
        val TAGS_DEVICE_LIST_FRAGMENT = "TAGS_DEVICE_LIST_FRAGMENT"
        val TAGS_FLOW_LIST_FRAGMENT = "TAGS_FLOW_LIST_FRAGMENT"
        val TAGS_SETTINGS_FRAGMENT = "TAGS_SETTINGS_FRAGMENT"
        val DATA_TYPE_CONFIG = "config"
        val DATA_TYPE_OPERATIONAL = "operational"
        val NODE_LIST = "NODE_LIST"
        val NODE_DATA = "NODE_DATA"
        val ADD_MODE = "ADD_MODE"
        val MODE_ADD = "MODE_ADD"
        val MODE_EDIT = "MODE_EDIT"

        val FLOW_POSITION = "FLOW_POSITION"
    }

    object RequestCode {
        val OPEN_ADD_ACTIVITY = 1001
        val OPEN_DETAILS_ACTIVITY = 1002
    }
}