package com.yggdralisk.meetme.api.interfaces

import com.yggdralisk.meetme.api.models.EventModel
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Jan Stoltman on 4/8/18.
 */
interface EventInterface {
    @GET("/api/Events")
    fun getEvents(): Call<List<EventModel>>

    @GET("api/Events/My")
    fun getMyEvents() : Call<List<EventModel>>

    @GET("/api/Events/{id}")
    fun getEventById(@Path("id") id: Int): Call<EventModel>

    @POST("/api/Events")
    fun postEvent(@Body event: EventModel): Call<EventModel>

    @POST("/api/Events/{id}/Join")
    fun joinEvent(@Path("id") id: Int): Call<EventModel>

    @POST("/api/Events/{id}/Leave")
    fun leaveEvent(@Path("id") id: Int): Call<EventModel>

    @DELETE("/api/Events/{id}")
    fun deleteEvent(@Path("id") id: Int): Call<EventModel>

    @POST("api/Events/{id}/Rate")
    fun rateEvent(@Path("id") id: Int, @Query("grade") grade: Float): Call<Float>
}