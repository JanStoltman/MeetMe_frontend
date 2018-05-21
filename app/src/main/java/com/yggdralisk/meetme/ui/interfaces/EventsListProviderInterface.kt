package com.yggdralisk.meetme.ui.interfaces

import com.yggdralisk.meetme.api.MyCallback
import com.yggdralisk.meetme.api.models.EventModel

/**
 * Created by Jan Stoltman on 4/16/18.
 */
interface EventsListProviderInterface {
    fun getEvents(): ArrayList<EventModel>
    fun getMyEvents(): ArrayList<EventModel>

    fun callEvents()
}