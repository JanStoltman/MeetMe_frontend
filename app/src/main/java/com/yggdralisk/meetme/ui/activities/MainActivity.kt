package com.yggdralisk.meetme.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.api.MyCallback
import com.yggdralisk.meetme.api.calls.EventCalls
import com.yggdralisk.meetme.api.models.EventModel
import com.yggdralisk.meetme.ui.EventsPagerAdapter
import com.yggdralisk.meetme.ui.interfaces.EventsListProviderInterface
import kotlinx.android.synthetic.main.activity_events.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : FragmentActivity(), EventsListProviderInterface {
    companion object {
        val events: ArrayList<EventModel> = ArrayList()
        val myEvents: ArrayList<EventModel> = ArrayList()
    }

    var pagerAdapter: EventsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        pagerAdapter = EventsPagerAdapter(supportFragmentManager, this, this)

        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = 2
        tabLayout.setupWithViewPager(viewPager)
        setupTabIcons()

        callEvents()
        callMyEvents()

        addEventButton.setOnClickListener({
            startAddEventActivity()
        })
    }

    override fun onResume() {
        super.onResume()
        callEvents()
    }

    private fun startAddEventActivity() {
        val intent = Intent(this, AddEventActivity::class.java)

        startActivity(intent)
    }

    override fun callEvents() {
        EventCalls.getEvents(object : MyCallback<List<EventModel>>(this) {
            override fun onResponse(call: Call<List<EventModel>>?, response: Response<List<EventModel>>?) {
                if (response != null && response.isSuccessful && response.body() != null) {
                    events.clear()
                    events.addAll(response.body() as List<EventModel>)
                    pagerAdapter?.refreshEvents()
                }
            }
        })
    }

    private fun callMyEvents() {
        EventCalls.getMyEvents(object : MyCallback<List<EventModel>>(this) {
            override fun onResponse(call: Call<List<EventModel>>?, response: Response<List<EventModel>>?) {
                if (response != null && response.isSuccessful && response.body() != null) {
                    myEvents.clear()
                    myEvents.addAll(response.body() as List<EventModel>)
                    pagerAdapter?.refreshEvents()
                }
            }
        })
    }

    override fun getEvents(): ArrayList<EventModel> {
        return events
    }

    override fun getMyEvents(): ArrayList<EventModel> {
        return myEvents
    }

    private fun setupTabIcons() {
        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_map_white_24dp);
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_list_white_24dp);
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_face_white_24dp);
    }
}
