package com.yggdralisk.meetme.ui.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.ui.AbstractEventsListAdapter
import com.yggdralisk.meetme.ui.interfaces.EventsListProviderInterface
import com.yggdralisk.meetme.utility.TimestampManager
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
            recyclerView?.adapter = EventsAdapter(context!!, provider!!)
        }

        view?.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)?.setOnRefreshListener {
            provider?.callEvents()
            provider?.callMyEvents()
            view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout).isRefreshing = false
        }

        return view
    }

    class EventsAdapter(context: Context, provider: EventsListProviderInterface)
        : AbstractEventsListAdapter(context, provider) {

        override fun onBindViewHolder(holder: AbstractEventsListAdapter.ViewHolder, position: Int) {
            val event = Companion.provider?.getEvents()?.getOrNull(position)
            bindEvent(event, holder)
        }

        override fun getItemCount(): Int {
            return provider.getEvents().size
        }
    }

    fun refreshEvents() {
        recyclerView?.adapter?.notifyDataSetChanged()
    }
}
