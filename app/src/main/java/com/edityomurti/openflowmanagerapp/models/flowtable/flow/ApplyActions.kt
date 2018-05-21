package com.edityomurti.openflowmanagerapp.models.flowtable.flow

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ApplyActions(
        @SerializedName("action")
        var actionData: MutableList<Action>
): Serializable