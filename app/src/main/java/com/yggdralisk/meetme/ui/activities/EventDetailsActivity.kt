package com.yggdralisk.meetme.ui.activities

import android.content.Context
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
import com.yggdralisk.meetme.MockApplication
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.api.models.EventModel
import com.yggdralisk.meetme.api.models.User
import kotlinx.android.synthetic.main.activity_event_details.*


class EventDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        const val EVENT_ID = "event_id"
    }

    var eventToDisplay: EventModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val eventId = intent.getIntExtra(EVENT_ID, 1) //TODO: Change this shit
        eventToDisplay = MockApplication.mockEvents.find { event -> event.id == eventId }

        popoulateUI()
        (mapView as SupportMapFragment).getMapAsync(this)

        if (eventToDisplay?.guestLimit!! <= eventToDisplay?.guests?.size!!) joinButton.text = this.getText(R.string.event_full)

        joinButton.setOnClickListener({ Toast.makeText(this, "Empty stub", Toast.LENGTH_SHORT).show()})

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.setMinZoomPreference(14f)

        val loc = eventToDisplay?.location
        val pos = LatLng(loc?.lat!!, loc.lon!!)//TODO: handle lack of permission
        val marker: MarkerOptions = MarkerOptions()
                .position(pos)

        googleMap?.addMarker(marker)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(pos))
    }

    private fun popoulateUI() {
        eventName.text = eventToDisplay?.name ?: "Error"

        val creator = MockApplication.mockUsers.find { event -> event.id == eventToDisplay?.id ?: 1 }
        creatorName.text = creator?.name ?: "Error"

        val ageRestriction = eventToDisplay?.ageRestriction
        ageRestrictions.text = "Age: min.${ageRestriction?.minAge} max.${ageRestriction?.maxAge}"

        guestsList.adapter = MyAdapter(eventToDisplay?.guests, this)
    }


    class MyAdapter(val guests: List<Int>?, val context: Context) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = convertView
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.guest_list_element, parent, false)
            }

            val currentItem = getItem(position) as User?
            view?.findViewById<TextView>(R.id.userName)?.text = currentItem?.name ?: "Error"
            return view!!
        }

        override fun getItem(position: Int): Any? {
            return MockApplication.mockUsers.find { user -> user.id == guests?.get(position) }
        }

        override fun getItemId(position: Int): Long {
            return guests?.get(position)?.toLong()!!
        }

        override fun getCount(): Int {
            return guests?.size ?: 0
        }

    }

}
