package com.yggdralisk.meetme.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Jan Stoltman on 6/10/18.
 */
data class ImgurPhotoModel(
        @SerializedName("data") var data: ImgurPhotoDataModel
)

data class ImgurPhotoDataModel(
        @SerializedName("link") var link: String
)