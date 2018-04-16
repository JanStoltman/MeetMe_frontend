package com.yggdralisk.meetme.api.interfaces

import com.yggdralisk.meetme.api.models.EventModel
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Jan Stoltman on 4/8/18.
 */
interface EventInterface {
    @GET("/api/Events")
    fun getEvents() : Call<List<EventModel>>
}