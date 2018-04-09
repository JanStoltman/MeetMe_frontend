package com.yggdralisk.meetme.api.interfaces

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Jan Stoltman on 4/7/18.
 */
interface UtilityInterface{
    @GET("/api/ping")
    fun ping() : Call<ResponseBody>
}