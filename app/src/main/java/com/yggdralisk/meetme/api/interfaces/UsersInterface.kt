package com.yggdralisk.meetme.api.interfaces

import com.yggdralisk.meetme.api.models.RegisterModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by Jan Stoltman on 4/7/18.
 * Login flow:
 *
 */
interface UsersInterface {
    @POST("/api/Account/Register")
    fun registerUser(@Body registerModel: RegisterModel): Call<ResponseBody>

    @GET("/api/MyId")
    fun getMyId(): Call<String?>
}