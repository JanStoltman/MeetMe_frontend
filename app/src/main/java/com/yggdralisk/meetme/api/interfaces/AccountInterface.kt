package com.yggdralisk.meetme.api.interfaces

import com.yggdralisk.meetme.api.models.ExternalLogin
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by Jan Stoltman on 4/7/18.
 * Login flow:
 *
 */
interface AccountInterface{
    @GET("/api/Account/ExternalLogins?returnUrl=%2F&generateState=true")
    fun getPossibleExternalLogins() : Call<List<ExternalLogin>>

    @GET
    fun redirectToLogin(@Url url: String): Call<ResponseBody>
}