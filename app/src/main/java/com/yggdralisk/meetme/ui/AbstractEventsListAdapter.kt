package com.yggdralisk.meetme.ui

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.R.id.creatorName
import com.yggdralisk.meetme.api.MyCallback
import com.yggdralisk.meetme.api.calls.UsersCalls
import com.yggdralisk.meetme.api.models.EventModel
import com.yggdralisk.meetme.api.models.UserModel
import com.yggdralisk.meetme.ui.activities.EventDetailsActivity
import com.yggdralisk.meetme.ui.interfaces.EventsListProviderInterface
import retrofit2.Call
import retrofit2.Response

/**
 * Created by Jan Stoltman on 6/1/18.
 */
abstract class AbstractEventsListAdapter(val context: Context, val provider: EventsListProviderInterface)
    : RecyclerView.Adapter<AbstractEventsListAdapter.ViewHolder>() {
    class ViewHolder(val rowView: View) : RecyclerView.ViewHolder(rowView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.events_recycler_row, parent, false)

        return ViewHolder(rowView)
    }

    fun bindEvent(event: EventModel?, holder: ViewHolder) {
        event?.let {
            holder.rowView.findViewById<TextView>(R.id.eventName)?.text = event.name
            holder.rowView.findViewById<TextView>(R.id.placeName)?.text = event.locationName
            holder.rowView.findViewById<TextView>(R.id.takenToMaxPlaces)?.text = String.format("%d/%d", event.guests?.size
                    ?: 0+1, event.guestLimit ?: 0+1)
            holder.rowView.findViewById<TextView>(R.id.eventRating)?.text = ""

            if (event.guestLimit == null || event.guestLimit ?: 1 > event.guests?.size ?: 0) {
                holder.rowView.findViewById<TextView>(R.id.takenToMaxPlaces)?.setTextColor(context.resources.getColor(R.color.colorAccent))
            }

            event.creator?.let {
                getCreator(holder, it)
            }


            holder.rowView.setOnClickListener({
                val intent = Intent(context, EventDetailsActivity::class.java)
                intent.putExtra(EventDetailsActivity.EVENT_ID, event.id)
                context.startActivity(intent)
            })
        }
    }

    private fun getCreator(holder: ViewHolder, creatorId: Int) {
        UsersCalls.getUserById(creatorId, object : MyCallback<UserModel>(context) {
            override fun onResponse(call: Call<UserModel>?, response: Response<UserModel>?) {
                super.onResponse(call, response)
                holder.rowView.findViewById<TextView>(R.id.creatorName)?.text = "${response?.body()?.name ?: "Error"} ${response?.body()?.surname
                        ?: "Error"}"
            }
        })
    }
}
