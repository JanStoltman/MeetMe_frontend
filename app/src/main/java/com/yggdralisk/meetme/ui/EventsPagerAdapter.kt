package com.yggdralisk.meetme.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yggdralisk.meetme.ui.activities.EventDetailsActivity
import com.yggdralisk.meetme.ui.fragments.EventsListFragment
import com.yggdralisk.meetme.ui.fragments.MyEventsListFragment
import com.yggdralisk.meetme.ui.interfaces.EventsListProviderInterface
import java.lang.ref.WeakReference

/**
 * Created by Jan Stoltman on 4/16/18.
 */
class EventsPagerAdapter(fm: FragmentManager, private val context: Context, private val provider: EventsListProviderInterface) : FragmentPagerAdapter(fm) {
    companion object {
        const val PAGES_COUNT = 3 //Map, List, UserPage
        const val ONE_SECOND_MILIS: Long = 2000L
        const val INITIAL_LATITUDE: Double = 51.108081
        const val INITIAL_LONGITUDE: Double = 17.065134
        const val INITIAL_MAP_ZOOM: Float = 10f
        private var markerId: String? = null
        private val countDownTimer = object : CountDownTimer(ONE_SECOND_MILIS, ONE_SECOND_MILIS) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                markerId = null
            }
        }

        var googleMapRef: GoogleMap? = null
        var eventsListFragmentRef: WeakReference<EventsListFragment>? = null
        var myEventsListFragmentRef: WeakReference<MyEventsListFragment>? = null
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                val supportMapFragment: SupportMapFragment = SupportMapFragment.newInstance()
                supportMapFragment.getMapAsync { googleMap ->
                    googleMapRef = googleMap
                    addEventsToMap(googleMap)

                    googleMap.setOnMarkerClickListener { marker ->
                        if (markerId == marker.id) {
                            val intent = Intent(context, EventDetailsActivity::class.java)
                            val eventId = marker?.tag as Int
                            intent.putExtra(EventDetailsActivity.EVENT_ID, eventId)
                            context.startActivity(intent)
                            true
                        } else {
                            markerId = marker.id
                            countDownTimer.start()
                            false
                        }
                    }

                    googleMap.setOnInfoWindowClickListener { marker ->
                        val intent = Intent(context, EventDetailsActivity::class.java)
                        val eventId = marker?.tag as Int
                        intent.putExtra(EventDetailsActivity.EVENT_ID, eventId)
                        context.startActivity(intent)
                    }

                    googleMap.isMyLocationEnabled = (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED)

                    val latLng = LatLng(INITIAL_LATITUDE, INITIAL_LONGITUDE)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, INITIAL_MAP_ZOOM))
                }

                return supportMapFragment
            } //Map
            1 -> {
                eventsListFragmentRef = WeakReference(EventsListFragment.newInstance(provider))
                eventsListFragmentRef?.get()!!
            } //Events list
            2 -> {
                myEventsListFragmentRef = WeakReference(MyEventsListFragment.newInstance(provider))
                myEventsListFragmentRef?.get()!!
            } //MyEvents list
            else -> throw IndexOutOfBoundsException(position.toString() + " :View pager position not found")
        }
    }

    private fun addEventsToMap(googleMap: GoogleMap?) {
        googleMap?.clear()

        for (event in provider.getEvents()) {
            val pos = LatLng(event.latitude!!, event.longitude!!) //TODO: check if lat/lon is null and throw error if true
            val markerOptions: MarkerOptions = MarkerOptions()
                    .position(pos)
                    .title(String.format("%s", event.name))
                    .snippet(String.format("%d / %d", event.guests?.size, event.guestLimit))

            if (event.guestLimit == null || event.guests == null || event.guestLimit!! > event.guests!!.size) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            }


            val marker = googleMap?.addMarker(markerOptions)
            marker?.tag = event.id
        }
    }

    override fun getCount(): Int {
        return PAGES_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "" //No title so there are only icons in tabLayout
    }

    fun refreshEvents() {
        //TODO: Remove events from map
        if (googleMapRef != null) {
            addEventsToMap(googleMapRef)
        }

        if (eventsListFragmentRef != null && eventsListFragmentRef!!.get() != null) {
            eventsListFragmentRef?.get()?.refreshEvents()
        }

        if (myEventsListFragmentRef != null && myEventsListFragmentRef!!.get() != null) {
            myEventsListFragmentRef?.get()?.refreshEvents()
        }
    }

}