package com.edityomurti.openflowmanagerapp.models.flowtable.flow

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class InstructionData(
        @SerializedName("instruction")
        var instruction: MutableList<Instruction>?
): Serializable