package com.yggdralisk.meetme.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.yggdralisk.meetme.MyApplication
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.api.MyCallback
import com.yggdralisk.meetme.api.calls.EventCalls
import com.yggdralisk.meetme.api.calls.UsersCalls
import com.yggdralisk.meetme.api.models.EventModel
import com.yggdralisk.meetme.api.models.SimpleUserModel
import com.yggdralisk.meetme.api.models.UserModel
import kotlinx.android.synthetic.main.activity_event_details.*
import retrofit2.Call
import retrofit2.Response


class EventDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        const val EVENT_ID = "event_id"
    }

    var eventToDisplay: EventModel? = null
    var eventGuests: ArrayList<SimpleUserModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val eventId = intent.getIntExtra(EVENT_ID, 1) //TODO: Change this shit
        EventCalls.getEventById(eventId, object : MyCallback<EventModel>(this) {
            override fun onResponse(call: Call<EventModel>?, response: Response<EventModel>?) {
                super.onResponse(call, response)
                eventToDisplay = response?.body()
                getEvent()
            }
        })
    }

    private fun getEvent() {
        popoulateUI()
        (mapView as SupportMapFragment).getMapAsync(this)


        if (MyApplication.userId in eventToDisplay?.guests ?: listOf()) {
            joinButton.text = getString(R.string.leave_event)

            joinButton.setOnClickListener({
                eventToDisplay?.let {
                    EventCalls.joinEvent(eventToDisplay?.id!!, object : MyCallback<EventModel>(this) {
                        override fun onResponse(call: Call<EventModel>?, response: Response<EventModel>?) {
                            super.onResponse(call, response)
                            if (response?.isSuccessful == true) {
                                Toast.makeText(baseContext, "Event left empty stub", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                }
            })
        } else if (eventToDisplay?.guestLimit ?: 4 <= eventToDisplay?.guests?.size ?: 1) {
            joinButton.text = this.getText(R.string.event_full)
        } else {
            joinButton.setOnClickListener({
                eventToDisplay?.let {
                    EventCalls.joinEvent(eventToDisplay?.id!!, object : MyCallback<EventModel>(this) {
                        override fun onResponse(call: Call<EventModel>?, response: Response<EventModel>?) {
                            super.onResponse(call, response)
                            if (response?.isSuccessful == true) {
                                Toast.makeText(baseContext, "Event joined", Toast.LENGTH_LONG).show()
                                this@EventDetailsActivity.finish()
                            }
                        }
                    })
                }
            })
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.setMinZoomPreference(14f)

        val pos = LatLng(eventToDisplay?.latitude ?: 51.108081, eventToDisplay?.longitude
                ?: 17.065134)//TODO: handle lack of permission
        val marker: MarkerOptions = MarkerOptions()
                .position(pos)

        googleMap?.addMarker(marker)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(pos))
    }

    private fun popoulateUI() {
        eventName.text = eventToDisplay?.name ?: ""
        locationName.text = eventToDisplay?.locationName ?: ""
        eventDescription.text = eventToDisplay?.description ?: ""
        ImageLoader.getInstance()
                .displayImage(eventToDisplay?.qrCodeLink?.code ?:
                "https://chart.googleapis.com/chart?cht=qr&chl=https%3A%2F%2Fwww.google.com%2Fmaps&chs=180x180&choe=UTF-8&chld=L|2",
                        qrCodeImage)

        getCreator()

        val ageRestriction = eventToDisplay?.ageRestriction
        ageRestrictions.text = "Age: min.${ageRestriction?.minAge} max.${ageRestriction?.maxAge}"

        guestsList.adapter = MyAdapter(eventGuests, this)
        getGuests()
    }

    private fun getGuests() {
        UsersCalls.getNamesForIds(eventToDisplay?.guests
                ?: listOf(), object : MyCallback<List<SimpleUserModel>>(this) {
            override fun onResponse(call: Call<List<SimpleUserModel>>?, response: Response<List<SimpleUserModel>>?) {
                super.onResponse(call, response)
                eventGuests.addAll(response?.body() ?: listOf())
                (guestsList?.adapter as MyAdapter).notifyDataSetChanged()
            }
        })
    }

    private fun getCreator() {
        UsersCalls.getUserById(eventToDisplay?.creator ?: 1, object : MyCallback<UserModel>(this) {
            override fun onResponse(call: Call<UserModel>?, response: Response<UserModel>?) {
                super.onResponse(call, response)
                creatorName.text = "${response?.body()?.name ?: "Error"} ${response?.body()?.surname ?: "Error"}"
            }
        })
    }

    class MyAdapter(val guests: List<SimpleUserModel>, val context: Context) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var view = convertView
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.guest_list_element, parent, false)
            }

            val currentItem = getItem(position) as SimpleUserModel
            view?.findViewById<TextView>(R.id.userName)?.text = "$currentItem.firstName ${currentItem.lastName}"

            view?.setOnClickListener {
                val intent = Intent(context, UserProfileActivity::class.java)
                intent.putExtra(UserProfileActivity.USER_ID, currentItem.id)
                context.startActivity(intent)
            }

            return view
        }

        override fun getItem(position: Int): Any? = guests[position]

        override fun getItemId(position: Int): Long = guests[position].id.toLong()

        override fun getCount(): Int = guests.size
    }
}
