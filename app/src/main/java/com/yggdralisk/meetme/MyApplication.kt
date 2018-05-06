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
    }

    private fun initImageLoader(applicationContext: Context?) {
        val config = ImageLoaderConfiguration.Builder(applicationContext)
        ImageLoader.getInstance().init(config.build())
    }
}