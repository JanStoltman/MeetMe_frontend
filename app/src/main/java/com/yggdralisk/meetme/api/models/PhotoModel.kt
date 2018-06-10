package com.yggdralisk.meetme.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Jan Stoltman on 6/10/18.
 */
data class PhotoModel(
        @SerializedName("Id") var id: Int = 0,
        @SerializedName("Url") var url: String = "",
        @SerializedName("EventId") var eventId: Int = 0
){
    companion object {
        fun fromImgurPhoto(photo: ImgurPhotoModel?): PhotoModel {
            return PhotoModel(id = 0, url = photo?.data?.link ?: "", eventId = 0)
        }
    }
}


