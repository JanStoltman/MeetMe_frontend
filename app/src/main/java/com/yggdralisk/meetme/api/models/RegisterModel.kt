package com.yggdralisk.meetme.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Jan Stoltman on 4/23/18.
 */
data class RegisterModel(@SerializedName("FirstName") var name: String? = "",
                         @SerializedName("LastName") var surname: String? = "",
                         @SerializedName("Email") var email: String? = "",
                         @SerializedName("PhoneNumber") var phoneNumber: String? = "",
                         @SerializedName("Description") var bio: String? = "",
                         @SerializedName("PhotoURL") var photoImage: String? = "",
                         @SerializedName("Token") var token: String? = "",
                         @SerializedName("RefreshToken") var refreshToken: String? = "",
                         @SerializedName("TokenExpirationDate") var tokenExpDate: Long = 0)