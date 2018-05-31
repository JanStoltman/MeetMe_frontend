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
import android.widget.TextView
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.ui.activities.EventDetailsActivity
import com.yggdralisk.meetme.ui.activities.EventDetailsActivity.Companion.EVENT_ID
import com.yggdralisk.meetme.ui.activities.UserDataFillActivity
import com.yggdralisk.meetme.ui.interfaces.EventsListProviderInterface
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
        val view: View? = inflater?.inflate(R.layout.my_events_list_fragment, container, false)
        val recyclerView: RecyclerView? = view?.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = EventsAdapter(context!!)

        view?.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)?.setOnRefreshListener {
            provider?.callEvents()
            provider?.callMyEvents()
            view?.findViewById<SwipeRefreshLayout>(R.id.refreshLayout).isRefreshing = false
        }

        view?.findViewById<ImageButton>(R.id.profileButton)?.setOnClickListener({
            val intent = Intent(context, UserDataFillActivity::class.java)
            intent.putExtras(Bundle.EMPTY)
            startActivity(intent)
        })

        return view
    }

    class EventsAdapter(val context: Context)
        : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
        class ViewHolder(val rowView: View) : RecyclerView.ViewHolder(rowView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val rowView: View = LayoutInflater.from(parent?.context)
                    .inflate(R.layout.recycler_row, parent, false)

            return ViewHolder(rowView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val event = provider?.getMyEvents()?.get(position)
            holder.rowView.findViewById<TextView>(R.id.eventName)?.text = event?.name
            holder.rowView.findViewById<TextView>(R.id.placeName)?.text = event?.locationName
            holder.rowView.findViewById<TextView>(R.id.takenToMaxPlaces)?.text = String.format("%d/%d", event?.guests?.size!! + 1, event?.guestLimit!! + 1)

            if (event?.guestLimit == null || event.guests == null || event.guestLimit!! > event.guests!!.size) {
                holder.rowView.findViewById<TextView>(R.id.takenToMaxPlaces)?.setTextColor(context.resources.getColor(R.color.colorAccent))
            }

/*            val creator: UserModel = MockApplication.mockUsers.filter { user -> user.id == event?.creator }[0]
            holder?.rowView?.findViewById<TextView>(R.id.creatorName)?.text = String.format("%s %s", creator.name, creator.surname)
            holder?.rowView?.findViewById<TextView>(R.id.creatorScore)?.text = String.format("%.2f", creator.rating)*/

            holder?.rowView?.setOnClickListener({
                val intent = Intent(context, EventDetailsActivity::class.java)
                intent.putExtra(EVENT_ID, event?.id)
                context.startActivity(intent)
            })
        }

        override fun getItemCount(): Int {
            return provider?.getMyEvents()?.size ?: 0
        }
    }

    fun refreshEvents() {
        recyclerView?.adapter?.notifyDataSetChanged()
    }
}
