package com.yggdralisk.meetme.api.models

/**
 * Created by Jan Stoltman on 4/9/18.
 */
data class UserModel(var id: Int?, var name: String?, var surname: String?, var email: String?,
                     var phoneNumber: String?, var birthDay: Long, var bio: String?, var photoImage: String?,
                     var rating: Double?)