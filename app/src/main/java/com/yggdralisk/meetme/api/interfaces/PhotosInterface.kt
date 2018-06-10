package com.yggdralisk.meetme.api.interfaces

import com.yggdralisk.meetme.api.models.PhotoModel
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Jan Stoltman on 6/10/18.
 */
interface PhotosInterface {
    @GET("/api/Photos/{id}")
    fun getPhotoById(@Path("id") photoId: Int): Call<PhotoModel>

    @DELETE("/api/Photos/{id}")
    fun deletePhotoById(@Path("id") photoId: Int): Call<PhotoModel>
}