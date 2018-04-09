package com.yggdralisk.meetme.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yggdralisk.meetme.R

class EventDetailsActivity : AppCompatActivity() {
    companion object {
        const val EVENT_ID = "event_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
    }
}
