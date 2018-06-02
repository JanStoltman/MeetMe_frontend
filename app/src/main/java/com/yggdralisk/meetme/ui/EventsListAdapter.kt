package com.yggdralisk.meetme.ui

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.ui.activities.EventDetailsActivity
import com.yggdralisk.meetme.ui.fragments.EventsListFragment
import com.yggdralisk.meetme.ui.interfaces.EventsListProviderInterface

/**
 * Created by Jan Stoltman on 6/1/18.
 */
open class EventsListAdapter(val context: Context, val provider: EventsListProviderInterface)
    : RecyclerView.Adapter<EventsListAdapter.ViewHolder>() {
    class ViewHolder(val rowView: View) : RecyclerView.ViewHolder(rowView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.events_recycler_row, parent, false)

        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = provider.getEvents().getOrNull(position)
        event?.let {
            holder.rowView.findViewById<TextView>(R.id.eventName)?.text = event.name
            holder.rowView.findViewById<TextView>(R.id.placeName)?.text = event.locationName
            holder.rowView.findViewById<TextView>(R.id.takenToMaxPlaces)?.text = String.format("%d/%d", event.guests?.size
                    ?: 0+1, event.guestLimit ?: 0+1)
            holder.rowView.findViewById<TextView>(R.id.eventRating)?.text = if (event.rating == 0.0) "" else event.rating.toString()

            if (event.guestLimit == null || event.guestLimit ?: 1 > event.guests?.size ?: 0) {
                holder.rowView.findViewById<TextView>(R.id.takenToMaxPlaces)?.setTextColor(context.resources.getColor(R.color.colorAccent))
            }

            holder.rowView.setOnClickListener({
                val intent = Intent(context, EventDetailsActivity::class.java)
                intent.putExtra(EventDetailsActivity.EVENT_ID, event.id)
                context.startActivity(intent)
            })
        }
    }

    override fun getItemCount(): Int {
        return provider.getEvents().size
    }
}
