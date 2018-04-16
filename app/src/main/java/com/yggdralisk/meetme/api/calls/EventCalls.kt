package com.yggdralisk.meetme.api.calls

import com.yggdralisk.meetme.api.APIGenerator
import com.yggdralisk.meetme.api.interfaces.EventInterface
import com.yggdralisk.meetme.api.models.EventModel
import retrofit2.Callback

/**
 * Created by Jan Stoltman on 4/8/18.
 */
class EventCalls {
    companion object {
        fun getEvents(callback: Callback<List<EventModel>>) {
            val call = APIGenerator.createService(EventInterface::class.java).getEvents()
            call.enqueue(callback)
        }
    }
}