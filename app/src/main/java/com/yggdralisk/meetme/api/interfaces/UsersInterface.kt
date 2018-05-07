package com.yggdralisk.meetme.api.interfaces

import com.yggdralisk.meetme.api.models.SimpleUserModel
import com.yggdralisk.meetme.api.models.UserModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Jan Stoltman on 4/7/18.
 * Login flow:
 *
 */
interface UsersInterface {
    @POST("/api/Users/Register")
    fun registerUser(@Body body: HashMap<String, Any>): Call<Int>

    @GET("/api/MyId")
    fun getMyId(): Call<Int>

    @GET("/api/Users")
    fun getUsers(): Call<List<UserModel>>

    @GET("/api/Users/{id}")
    fun getUserById(@Path("id") id: Int): Call<UserModel>

    @PUT("/api/Users/{id}")
    fun updateMyData(@Body userModel: UserModel, @Path("id") id:Int): Call<ResponseBody>

    @POST("/api/Users")
    fun getUsersNameFromIds(@Body ids: List<Int>): Call<List<SimpleUserModel>>
}