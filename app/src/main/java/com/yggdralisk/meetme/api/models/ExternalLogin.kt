package com.yggdralisk.meetme.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Jan Stoltman on 4/7/18.
 */
data class ExternalLogin (
        @SerializedName("Name") var name: String,
        @SerializedName("Url") var url: String,
        @SerializedName("State") var state: String
)