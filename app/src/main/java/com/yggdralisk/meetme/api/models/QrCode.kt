package com.yggdralisk.meetme.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Jan Stoltman on 4/8/18.
 */
data class QrCode(
        @SerializedName("Code") var code: String?
)