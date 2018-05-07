package com.yggdralisk.meetme.api.interfaces

import com.yggdralisk.meetme.api.models.EventModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Jan Stoltman on 4/8/18.
 */
interface EventInterface {
    @GET("/api/Events")
    fun getEvents() : Call<List<EventModel>>

    @GET("/api/Events/{id}")
    fun getEventById(@Path("id") id:Int) : Call<EventModel>

    @GET("api/Events/My")
    fun getMyEvents() : Call<List<EventModel>>
}