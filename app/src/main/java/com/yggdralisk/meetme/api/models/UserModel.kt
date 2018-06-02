package com.yggdralisk.meetme.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Jan Stoltman on 4/9/18.
 */
data class UserModel(@SerializedName("Id") var id: Int?,
                     @SerializedName("Token") var token: String?,
                     @SerializedName("FirstName") var name: String?,
                     @SerializedName("LastName") var surname: String?,
                     @SerializedName("Email")  var email: String?,
                     @SerializedName("PhoneNumber") var phoneNumber: String?,
                     @SerializedName("Description") var bio: String?,
                     @SerializedName("PhotoURL") var photoImage: String?,
                     @SerializedName("Rating") var rating: Double?,
                     @SerializedName("FacebookURL") var String: String?
)
