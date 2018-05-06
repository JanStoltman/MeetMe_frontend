package com.yggdralisk.meetme.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Jan Stoltman on 5/6/18.
 */
data class SimpleUserModel(
        @SerializedName("Id")val id: Int,
        @SerializedName("FirstName") val firstName: String,
        @SerializedName("LastName") val lastName: String
)