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
import android.widget.ImageButton
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.ui.AbstractEventsListAdapter
import com.yggdralisk.meetme.ui.activities.UserDataFillActivity
import com.yggdralisk.meetme.ui.interfaces.EventsListProviderInterface
import com.yggdralisk.meetme.utility.TimestampManager
import kotlinx.android.synthetic.main.events_list_frgment.*

/**
 * Created by Maciek on 5/7/18.
 */
class MyEventsListFragment : Fragment() {
    companion object {
        var provider: EventsListProviderInterface? = null
        fun newInstance(provider: EventsListProviderInterface): MyEventsListFragment {
            this.provider = provider
            return MyEventsListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater.inflate(R.layout.my_events_list_fragment, container, false)
        val recyclerView: RecyclerView? = view?.findViewById(R.id.recyclerView)

        if (context != null && EventsListFragment.provider != null) {
            recyclerView?.layoutManager = LinearLayoutManager(context)
            recyclerView?.adapter = MyEventsAdapter(context!!, EventsListFragment.provider!!)
        }

        view?.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)?.setOnRefreshListener {
            provider?.callEvents()
            provider?.callMyEvents()
            view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout).isRefreshing = false
        }

        return view
    }

    class MyEventsAdapter(context: Context, provider: EventsListProviderInterface)
        :   AbstractEventsListAdapter(context, provider) {

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val event = provider.getMyEvents().getOrNull(position)

            bindEvent(event, holder)
            event?.let {
                val manager = TimestampManager(context)
                when {
                    manager.isTimestampInPast(event.endTime ?: 0) -> //Event ended
                        holder.rowView.findViewById<View>(R.id.event_row_background).setBackgroundColor(context.resources.getColor(R.color.grey))

                    manager.isTimestampInPast(event.startTime ?: 0) -> //Event ongoing
                        holder.rowView.findViewById<View>(R.id.event_row_background).setBackgroundColor(context.resources.getColor(R.color.yellow))

                    else -> {//Normal event -- NOP
                    }
                }
            }
        }

        override fun getItemCount(): Int {
            return provider.getMyEvents().size
        }
    }

    fun refreshEvents() {
        recyclerView?.adapter?.notifyDataSetChanged()
    }
}
