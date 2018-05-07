package com.yggdralisk.meetme

import android.app.Application
import android.content.Context
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.yggdralisk.meetme.api.models.*
import com.yggdralisk.meetme.utility.SharedPreferencesManager


/**
 * Created by Jan Stoltman on 4/9/18.
 */
class MyApplication : Application() {
    companion object {
        var userId: Int = 0
        var currentUser: UserModel? = null
    }
    override fun onCreate() {
        super.onCreate()
        initImageLoader(applicationContext)
        generateMockData()
    }

    private fun initImageLoader(applicationContext: Context?) {
        val config = ImageLoaderConfiguration.Builder(applicationContext)
        ImageLoader.getInstance().init(config.build())
    }

    private fun generateMockData() {
        //users
        val user1 = UserModel(1, "2342342342", "Maciek", "Szulc", "szulcmaciej1@gmail.com",
                "666555444", "student informatyki", "", 4.0)
        val user2 = UserModel(2, "sasfd2312dsfs2", "Jan", "Stoltman", "jan@gmail.com",
                "123654234", "student informatyki", "", 5.0)
        val user3 = UserModel(3, "sdfsdf23rrg34", "Michał", "Rypina", "michal@gmail.com",
                "695652376", "student informatyki", "", 4.5)
        val user4 = UserModel(4, "323rg23gfd", "Kamil", "Kolesiński", "kamil@gmail.com",
                "695324396", "student informatyki", "", 4.5)

        val userList: ArrayList<UserModel> = ArrayList()
        userList.add(user1)
        userList.add(user2)
        userList.add(user3)
        userList.add(user4)



        //events

        val guestList1 = mutableListOf<Int>()
        guestList1.add(1)
        guestList1.add(2)
        guestList1.add(3)
        guestList1.add(4)
        val guestList2 = mutableListOf<Int>()
        guestList2.add(2)
        val guestList3 = mutableListOf<Int>()
        guestList2.add(3)
        guestList2.add(4)

        val event1 = EventModel(1, 1525693836, 1525693899, 1525695000, null,
                "Catan na wyspie", guestList1, 1, 10, AgeRestriction(0, 100), EventType.PUBLIC,
                51.116311, 17.038048, "Wyspa Słodowa",
                "Zbieramy się na wyspie i gramy w Osadników z Catanu", "")
        val event2 = EventModel(2, 1525697836, 1525698899, 1525695000, null,
                "Integracja W-8", guestList2, 2, 100, AgeRestriction(18, 30), EventType.PUBLIC,
                51.104553, 17.068471, "Zazoo Beach Bar",
                "Izet integruje się w Zazoo", "")
        val event3 = EventModel(3, 1525697536, 1525695899, 1525699000, null,
                "Wyjście do ZOO", guestList3, 3, 15, AgeRestriction(5, 80), EventType.PUBLIC,
                51.105719, 17.076341, "ZOO",
                "Grupowe wyjście do Zoo we Wrocławiu", "")


        val eventList: ArrayList<EventModel> = ArrayList()
        eventList.add(event1)
        eventList.add(event2)
        eventList.add(event3)


        val sharedPrefsManager = SharedPreferencesManager()
        sharedPrefsManager.storeUsers(applicationContext, userList)
        sharedPrefsManager.storeEvents(applicationContext, eventList)
    }



}