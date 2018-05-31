package com.yggdralisk.meetme.api.calls

import android.view.ViewDebug
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

        fun getMyEvents(callback: Callback<List<EventModel>>) {
            val call = APIGenerator.createService(EventInterface::class.java).getMyEvents()
            call.enqueue(callback)
        }

        fun getEventById(id:Int, callback: Callback<EventModel>) {
            val call = APIGenerator.createService(EventInterface::class.java).getEventById(id)
            call.enqueue(callback)
        }

        fun postEvent(event:EventModel, callback: Callback<EventModel>) {
            val call = APIGenerator.createService(EventInterface::class.java).postEvent(event)
            call.enqueue(callback)
        }

        fun joinEvent(id:Int, callback: Callback<EventModel>) {
            val call = APIGenerator.createService(EventInterface::class.java).joinEvent(id)
            call.enqueue(callback)
        }

        fun leaveEvent(id:Int, callback: Callback<EventModel>) {
            val call = APIGenerator.createService(EventInterface::class.java).leaveEvent(id)
            call.enqueue(callback)
        }

        fun deleteEvent(id: Int, callback: Callback<EventModel>){
            val call = APIGenerator.createService(EventInterface::class.java).deleteEvent(id)
            call.enqueue(callback)
        }

        fun rateEvent(id: Int, grade: Float, callback: Callback<Float>){
            val call = APIGenerator.createService(EventInterface::class.java).rateEvent(id, grade)
            call.enqueue(callback)
        }
    }
}