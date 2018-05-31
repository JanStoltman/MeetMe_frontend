package com.yggdralisk.meetme.utility.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.widget.Toast
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.api.models.EventModel
import com.yggdralisk.meetme.ui.activities.EventDetailsActivity
import com.yggdralisk.meetme.utility.TimestampManager

/**
 * Created by Maciek on 2018-05-28.
 */

class NotificationHelper {
    companion object {
        val EVENT_REMINDER_CHANNEL = "event_reminder"

        fun remindUser(context: Context, eventModel: EventModel){
            val notificationTimeMilis = eventModel.startTime!! * 1000 - 1000 * 60 * 60

            val intent = Intent(context, MyReceiver::class.java)
            intent.putExtra(EventDetailsActivity.EVENT_ID, eventModel.id)
            val pendingIntent = PendingIntent.getBroadcast(context, eventModel.id!!, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTimeMilis, pendingIntent)

        }
    }

}