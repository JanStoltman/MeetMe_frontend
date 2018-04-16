package com.yggdralisk.meetme.utility

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.api.models.EventModel
import com.yggdralisk.meetme.api.models.UserModel

/**
 * Created by Jan Stoltman on 4/7/18.
 */
class SharedPreferencesManager {
    private fun getSharedPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(
                context.getString(R.string.shared_pref_file_key),
                Context.MODE_PRIVATE
        )
    }

    fun storeToken(context: Context, token: String) {
        val sharedPref = getSharedPref(context)

        with(sharedPref.edit()) {
            putString(context.getString(R.string.token_pref_key), token)
            commit()
        }
    }

    /**
     * @return String token if any is stored, null otherwise
     */
    fun getToken(context: Context?): String? {
        context ?: return null

        val sharedPref = getSharedPref(context)
        return sharedPref.getString(
                context.getString(R.string.token_pref_key), null
        )
    }

    /**
     * @return true if any token is being stored, false otherwise
     * and null if provided context is null
     */
    fun hasToken(context: Context?): Boolean? {
        context ?: return null
        val sharedPref = context.getSharedPreferences(
                context.getString(R.string.shared_pref_file_key),
                Context.MODE_PRIVATE
        )

        return (sharedPref.getString(
                context.getString(R.string.shared_pref_file_key),
                null) != null)
    }

    fun storeUsers(context: Context, users: List<UserModel>) {
        val sharedPref = getSharedPref(context)
        val json = Gson().toJson(users)

        with(sharedPref.edit()) {
            remove(context.getString(R.string.users_store_key))
            putString(context.getString(R.string.users_store_key), json)
            commit()
        }
    }

    fun getUsers(context: Context?): List<UserModel>? {
        context ?: return null

        val sharedPref = getSharedPref(context)
        val json = sharedPref.getString(context.getString(R.string.users_store_key), null)
        return if (json == null || json.isEmpty()) null else Gson().fromJson(json, object : TypeToken<List<UserModel>>() {}.type)
    }

    fun storeEvents(context: Context, eventModels: List<EventModel>) {
        val sharedPref = getSharedPref(context)
        val json = Gson().toJson(eventModels)

        with(sharedPref.edit()) {
            remove(context.getString(R.string.events_store_key))
            putString(context.getString(R.string.events_store_key), json)
            commit()
        }
    }

    fun getEvents(context: Context?): List<EventModel>? {
        context ?: return null

        val sharedPref = getSharedPref(context)
        val json = sharedPref.getString(context.getString(R.string.token_pref_key), null)
        return if (json == null || json.isEmpty()) null else Gson().fromJson(json, object : TypeToken<List<EventModel>>() {}.type)
    }
}