package com.yggdralisk.meetme.api.calls

import com.yggdralisk.meetme.api.APIGenerator
import com.yggdralisk.meetme.api.interfaces.UtilityInterface
import okhttp3.ResponseBody
import retrofit2.Callback

/**
 * Created by Jan Stoltman on 4/7/18.
 */
class UtilityCalls {
    companion object {
        fun ping(callback: Callback<ResponseBody>) {
            val call = APIGenerator.createService(UtilityInterface::class.java).ping()
            call.enqueue(callback)
        }
    }
}