package com.yggdralisk.meetme.ui.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yggdralisk.meetme.MockApplication
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.api.models.Event
import com.yggdralisk.meetme.ui.fragments.EventsListFragment
import com.yggdralisk.meetme.ui.fragments.UserProfileFragment
import kotlinx.android.synthetic.main.activity_events.*

class EventsActivity : FragmentActivity() {
    companion object {
        val events: ArrayList<Event> = ArrayList()
    }

    var pagerAdapter: EventsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        events.addAll(MockApplication.mockEvents)

        pagerAdapter = EventsPagerAdapter(supportFragmentManager, this)
        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = 2
        tabLayout.setupWithViewPager(viewPager)
        setupTabIcons()
    }

    private fun setupTabIcons() {
        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_map_white_24dp);
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_list_white_24dp);
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_face_white_24dp);
    }

    class EventsPagerAdapter(fm: FragmentManager, private val context: Context) : FragmentPagerAdapter(fm) {
        companion object {
            const val PAGES_COUNT = 3 //Map, List, UserPage
            const val ONE_SECOND_MILIS: Long = 2000L
            private var markerId: String? = null
            private val countDownTimer = object : CountDownTimer(ONE_SECOND_MILIS, ONE_SECOND_MILIS) {
                override fun onTick(millisUntilFinished: Long) {}

                override fun onFinish() {
                    markerId = null
                }
            }
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    val supportMapFragment: SupportMapFragment = SupportMapFragment.newInstance()
                    supportMapFragment.getMapAsync { googleMap ->
                        for (event in EventsActivity.events) {
                            val pos = LatLng(event.location?.lat!!, event.location?.lon!!) //TODO: check if lat/lon is null and throw error if true
                            val marker: MarkerOptions = MarkerOptions()
                                    .position(pos)
                                    .title(String.format("%d .%s", event.id, event.name))
                                    .snippet(String.format("%d - %d", event.ageRestriction?.minAge, event.ageRestriction?.maxAge))

                            if (event.guestLimit == null || event.guests == null || event.guestLimit!! > event.guests!!.size) {
                                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            }

                            googleMap.addMarker(marker)
                        }
                        googleMap.setOnMarkerClickListener { marker ->
                            if (markerId == marker.id) {
                                val intent = Intent(context, EventDetailsActivity::class.java)
                                intent.putExtra(EventDetailsActivity.EVENT_ID, marker.title.split(" ")[0].toInt())
                                context.startActivity(intent)
                                true
                            } else {
                                markerId = marker.id
                                countDownTimer.start()
                                false
                            }
                        }

                        googleMap.setMinZoomPreference(14f)

                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            googleMap.isMyLocationEnabled = true //TODO: move camera to user's location

                            val sydney = LatLng(51.108081, 17.065134)//TODO: Change this when ^that one is done
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                        } else {
                            val sydney = LatLng(51.108081, 17.065134)//TODO: handle lack of permission
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                        }
                    }

                    return supportMapFragment
                } //Map
                1 -> EventsListFragment() //Events list
                2 -> UserProfileFragment() //User profile
                else -> throw IndexOutOfBoundsException(position.toString() + " :View pager position not found")
            }
        }

        override fun getCount(): Int {
            return PAGES_COUNT
        }

        override fun getPageTitle(position: Int): CharSequence {
            return "" //No title so there are only icons in tabLayout
        }


    }
}
