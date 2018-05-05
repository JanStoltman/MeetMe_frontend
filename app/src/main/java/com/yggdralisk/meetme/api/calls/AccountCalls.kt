package com.yggdralisk.meetme.api.calls

import com.yggdralisk.meetme.api.APIGenerator
import com.yggdralisk.meetme.api.interfaces.UsersInterface
import com.yggdralisk.meetme.api.models.RegisterModel
import okhttp3.ResponseBody
import retrofit2.Callback

/**
 * Created by Jan Stoltman on 4/7/18.
 */
class AccountCalls {
    companion object {
        fun registerUser(registerModel: RegisterModel, callback: Callback<ResponseBody>) {
            val call = APIGenerator.createService(UsersInterface::class.java)
                    .registerUser(registerModel)
            call.enqueue(callback)
        }

        fun getMyId(callback: Callback<String?>) {
            val call = APIGenerator.createService(UsersInterface::class.java)
                    .getMyId()
            call.enqueue(callback)
        }
    }
}