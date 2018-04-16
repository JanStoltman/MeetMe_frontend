package com.yggdralisk.meetme.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yggdralisk.meetme.MockApplication
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.api.models.UserModel
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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater?.inflate(R.layout.events_list_frgment, container, false)
        val recyclerView: RecyclerView? = view?.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = EventsAdapter(context)

        return view
    }

    class EventsAdapter(val context: Context)
        : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
        class ViewHolder(val rowView: View) : RecyclerView.ViewHolder(rowView)

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val rowView: View = LayoutInflater.from(parent?.context)
                    .inflate(R.layout.recycler_row, parent, false)

            return ViewHolder(rowView)
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            val event = provider?.getEvents()?.get(position)
            holder?.rowView?.findViewById<TextView>(R.id.eventName)?.text = event?.name
            holder?.rowView?.findViewById<TextView>(R.id.takenToMaxPlaces)?.text = String.format("%d/%d", event?.guests?.size, event?.guestLimit)

            if (event?.guestLimit == null || event.guests == null || event.guestLimit!! > event.guests!!.size) {
                holder?.rowView?.findViewById<TextView>(R.id.takenToMaxPlaces)?.setTextColor(context.resources.getColor(R.color.colorAccent))
            }

            val creator: UserModel = MockApplication.mockUsers.filter { user -> user.id == event?.creator }[0]
            holder?.rowView?.findViewById<TextView>(R.id.creatorName)?.text = String.format("%s %s", creator.name, creator.surname)
            holder?.rowView?.findViewById<TextView>(R.id.creatorScore)?.text = String.format("%.2f", creator.rating)

            holder?.rowView?.setOnClickListener({
                val intent = Intent(context, EventDetailsActivity::class.java)
                intent.putExtra(EVENT_ID, event?.id)
                context.startActivity(intent)
            })
        }

        override fun getItemCount(): Int {
            return provider?.getEvents()?.size ?: 0
        }
    }

    fun refreshEvents() {
        recyclerView?.adapter?.notifyDataSetChanged()
    }
}
