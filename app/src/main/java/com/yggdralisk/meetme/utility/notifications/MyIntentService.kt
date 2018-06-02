package com.yggdralisk.meetme.utility.notifications

import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.api.MyCallback
import com.yggdralisk.meetme.api.calls.EventCalls
import com.yggdralisk.meetme.api.models.EventModel
import com.yggdralisk.meetme.ui.activities.EventDetailsActivity
import retrofit2.Call
import retrofit2.Response

/**
 * Created by Lenovo on 2018-05-31.
 */
class MyIntentService(name: String) : IntentService(name){
    constructor(): this("MyIntentService")

    override fun onHandleIntent(intent: Intent?) {
        //TODO what if app isn't running or user isn't logged in
        val eventId = intent!!.extras.get(EventDetailsActivity.EVENT_ID) as Int
        var eventModel: EventModel? = null

        EventCalls.getEventById(eventId, object : MyCallback<EventModel>(this) {
            override fun onResponse(call: Call<EventModel>?, response: Response<EventModel>?) {
                super.onResponse(call, response)
                eventModel = response?.body()
                createNotification(eventModel!!)
            }
        })
    }

    fun createNotification(eventModel: EventModel){
        val title = eventModel.name + " starts soon"
        var content = ""
        if(!eventModel.locationName.isNullOrBlank()){
            content = eventModel.name + " starts soon at " + eventModel.locationName
        }

        val intent = Intent(this, EventDetailsActivity::class.java)
        intent.putExtra(EventDetailsActivity.EVENT_ID, eventModel.id)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, NotificationHelper.EVENT_REMINDER_CHANNEL)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                //uncomment to enable navigation to event
                //.setContentIntent(pendingIntent)

                .setAutoCancel(true)
                .build()

        val notificationManager = NotificationManagerCompat.from(this)
        val notificationID = eventModel.id!!
        notificationManager.notify(notificationID, notification)
    }
}