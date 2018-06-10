package com.yggdralisk.meetme.api.interfaces

import com.yggdralisk.meetme.api.models.ImgurPhotoModel
import retrofit2.Call
import retrofit2.http.POST

/**
 * Created by Jan Stoltman on 6/10/18.
 */
interface ImgurInterface {
    @POST("/image")
    fun postImage(base64String: String): Call<ImgurPhotoModel>
}