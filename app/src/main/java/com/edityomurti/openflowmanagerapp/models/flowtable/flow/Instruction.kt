package com.edityomurti.openflowmanagerapp.models.flowtable.flow

import com.google.gson.annotations.SerializedName

data class Instruction(
        @SerializedName("order")
        var order: Int?,
        @SerializedName("apply-actions")
        var applyActions: ApplyActions?
)