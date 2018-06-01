package com.yggdralisk.meetme.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.ui.EventsListAdapter
import com.yggdralisk.meetme.ui.activities.EventDetailsActivity
import com.yggdralisk.meetme.ui.activities.EventDetailsActivity.Companion.EVENT_ID
import com.yggdralisk.meetme.ui.interfaces.EventsListProviderInterface
import kotlinx.android.synthetic.main.events_list_frgment.*

/**
 * Created by Jan Stoltman on 4/8/18.
 */
class EventsListFragment : Fragment() {
    companion object {
        var provider: EventsListProviderInterface? = null
        fun newInstance(provider: EventsListProviderInterface): EventsListFragment {
            this.provider = provider
            return EventsListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater.inflate(R.layout.events_list_frgment, container, false)
        val recyclerView: RecyclerView? = view?.findViewById(R.id.recyclerView)

        if(context != null && provider != null){
            recyclerView?.layoutManager = LinearLayoutManager(context)
            recyclerView?.adapter = EventsListAdapter(context!!, provider!!)
        }

        view?.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)?.setOnRefreshListener {
            provider?.callEvents()
            provider?.callMyEvents()
            view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout).isRefreshing = false
        }

        return view
    }

    fun refreshEvents() {
        recyclerView?.adapter?.notifyDataSetChanged()
    }
}
