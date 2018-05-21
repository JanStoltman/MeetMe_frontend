package com.yggdralisk.meetme.api

import android.content.Context
import android.widget.Toast
import com.yggdralisk.meetme.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Jan Stoltman on 4/7/18.
 */
abstract class MyCallback<T>(private val context: Context) : Callback<T> {
    override fun onFailure(call: Call<T>?, t: Throwable?) {
        Toast.makeText(context, R.string.no_internet_alert, Toast.LENGTH_LONG).show()
    }

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        if(response == null || !response.isSuccessful){
//            Toast.makeText(context, "Bad API response", Toast.LENGTH_LONG).show()
        }
    }
}