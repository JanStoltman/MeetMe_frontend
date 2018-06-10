package com.yggdralisk.meetme.api.calls

import com.yggdralisk.meetme.api.APIGenerator
import com.yggdralisk.meetme.api.interfaces.EventInterface
import com.yggdralisk.meetme.api.interfaces.PhotosInterface
import com.yggdralisk.meetme.api.models.EventModel
import com.yggdralisk.meetme.api.models.PhotoModel
import retrofit2.Callback

/**
 * Created by Jan Stoltman on 6/10/18.
 */
class PhotosCalls {
    fun getPhoto(id: Int, callback: Callback<PhotoModel>) {
        val call = APIGenerator.createService(PhotosInterface::class.java).getPhotoById(id)
        call.enqueue(callback)
    }

    fun deletePhoto(id:Int, callback: Callback<PhotoModel>) {
        val call = APIGenerator.createService(PhotosInterface::class.java).deletePhotoById(id)
        call.enqueue(callback)
    }
}