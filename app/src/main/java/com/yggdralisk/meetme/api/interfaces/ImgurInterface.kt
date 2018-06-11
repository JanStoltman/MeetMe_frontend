package com.yggdralisk.meetme.api.interfaces

import com.yggdralisk.meetme.api.models.ImgurPhotoModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Jan Stoltman on 6/10/18.
 */
interface ImgurInterface {
    @POST("/3/image")
    @FormUrlEncoded
    fun postImage(@Field("image") base64String: String): Call<ImgurPhotoModel>
}