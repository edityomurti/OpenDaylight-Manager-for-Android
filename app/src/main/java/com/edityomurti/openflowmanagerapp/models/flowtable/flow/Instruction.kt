package com.edityomurti.openflowmanagerapp.models.flowtable.flow

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Instruction(
        @SerializedName("order")
        var order: Int?,
        @SerializedName("apply-actions")
        var applyActions: ApplyActions?
): Serializable