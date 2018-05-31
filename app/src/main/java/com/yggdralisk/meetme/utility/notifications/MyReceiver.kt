package com.yggdralisk.meetme.utility.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by Lenovo on 2018-05-31.
 */
class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val intent1 = Intent(context, MyIntentService::class.java)
        intent1.putExtras(intent?.extras)
        context?.startService(intent1)
    }
}