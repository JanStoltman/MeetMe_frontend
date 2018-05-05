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
class MockApplication : Application() {
    companion object {
        val mockUsers: ArrayList<UserModel> = createUsers()
        val mockEvents: ArrayList<EventModel> = createEvents()

        private fun createUsers(): ArrayList<UserModel> {
            val l: ArrayList<UserModel> = ArrayList()

            l.add(UserModel(1, "Jan", "Stoltman", "stoltmanjan@gmail.com",
                    "985632458", "My bio", "https://cdn-images-1.medium.com/max/1200/0*UCnZgM0r8e-Apv1d.", 4.54, 0, "", "" , 0))

            l.add(UserModel(2, "Konrad", "Wallenrod", "k.wallenrod@gmail.com",
                    "985632458", "Konrad Wallenrod is an 1828 narrative poem, in Polish, by Adam Mickiewicz, set in the 14th-century Grand Duchy of Lithuania.",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/9/94/Alf_i_Halban.jpg/220px-Alf_i_Halban.jpg", 2.230, 0,"", "" , 0))

            l.add(UserModel(3, "Wojciech", "Wojcieszewski", "wszewski@gmail.com",
                    "985632458", "My bio", "", 5.000, 0,"", "" , 0))
            return l
        }

        private fun createEvents(): ArrayList<EventModel> {
            val l: ArrayList<EventModel> = ArrayList()

            l.add(EventModel(
                    1, "MockEvent1", listOf(2, 3), 1,
                    1523344142, 2, AgeRestriction(1, 100),
                    EventType.PUBLIC,
                    Location(1, 17.0604185, 51.110869, "Pasaż Grunwaldzki", "Pasaż", "https://www.google.com/maps/place/Pasa%C5%BC+Grunwaldzki/@51.1126243,17.057185,17z/data=!4m5!3m4!1s0x470fe82b590f994b:0x25b58f5939f1c6d5!8m2!3d51.112621!4d17.059379"),
                    1523344142, 1523351342, QrCode("")
            ))

            l.add(EventModel(
                    2, "MockEvent2", listOf(1), 2,
                    1523344142, 2, AgeRestriction(1, 100),
                    EventType.PRIVATE,
                    Location(2, 17.0562731, 51.1121472, "U Gruźina", "Restauracja", "https://www.google.com/maps/place/U+Gruzina/@51.1121472,17.0562731,19z/data=!4m5!3m4!1s0x470fe9d4f2e350b9:0x87fcb2c0770cef06!8m2!3d51.1121472!4d17.0568202"),
                    1523361600, 1523372400, QrCode("")
            ))

            return l
        }

        /**
         * Reads EVENT_MODELS from shared prefences and overwrites mockEvents
         */
        fun readEvents(context: Context) {
            val l = SharedPreferencesManager().getEvents(context)
            if (l != null && l.isNotEmpty()) {
                mockEvents.clear()
                mockEvents.addAll(l)
            }
        }

        fun saveEvents(context: Context) {
            SharedPreferencesManager().storeEvents(context, mockEvents)
        }

        /**
         * Reads users from shared prefences and overwrites mockEvents
         */
        fun readUsers(context: Context) {
            val l = SharedPreferencesManager().getUsers(context)
            if (l != null && l.isNotEmpty()) {
                mockUsers.clear()
                mockUsers.addAll(l)
            }
        }

        fun saveUsers(context: Context) {
            SharedPreferencesManager().storeUsers(context, mockUsers)
        }
    }

    override fun onCreate() {
        super.onCreate()
        initImageLoader(applicationContext)
        readEvents(this)
        readUsers(this)
    }

    private fun initImageLoader(applicationContext: Context?) {
        val config = ImageLoaderConfiguration.Builder(applicationContext)
        ImageLoader.getInstance().init(config.build())
    }
}