package com.yggdralisk.meetme.utility

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.yggdralisk.meetme.MyApplication
import com.yggdralisk.meetme.api.models.UserModel

import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Maciek on 2018-05-06.
 */

class FBProfileDataHelper {

    companion object {
        @Throws(JSONException::class)
        fun jsonToBundle(jsonObject: JSONObject): Bundle {
            val bundle = Bundle()
            val iter = jsonObject.keys()
            while (iter.hasNext()) {
                val key = iter.next() as String
                val value = jsonObject.getString(key)
                bundle.putString(key, value)
            }
            return bundle
        }
    }



}
