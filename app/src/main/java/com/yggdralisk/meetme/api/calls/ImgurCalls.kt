package com.yggdralisk.meetme.api.calls

import com.yggdralisk.meetme.api.ImgurAPIGenerator
import com.yggdralisk.meetme.api.interfaces.ImgurInterface
import com.yggdralisk.meetme.api.models.ImgurPhotoModel
import retrofit2.Callback

/**
 * Created by Jan Stoltman on 6/10/18.
 */
class ImgurCalls {
    companion object {
        fun postImage(base64String: String, callback: Callback<ImgurPhotoModel>){
            val call = ImgurAPIGenerator.createService(ImgurInterface::class.java).postImage(base64String)
            call.enqueue(callback)
        }
    }
}