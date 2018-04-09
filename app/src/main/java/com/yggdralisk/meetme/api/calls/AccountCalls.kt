package com.yggdralisk.meetme.api.calls

import com.yggdralisk.meetme.api.APIGenerator
import com.yggdralisk.meetme.api.interfaces.AccountInterface
import com.yggdralisk.meetme.api.models.ExternalLogin
import okhttp3.ResponseBody
import retrofit2.Callback

/**
 * Created by Jan Stoltman on 4/7/18.
 */
class AccountCalls {
    companion object {
        fun getPossibleExternalLogins(callback: Callback<List<ExternalLogin>>) {
            val call = APIGenerator.createService(AccountInterface::class.java).getPossibleExternalLogins()
            call.enqueue(callback)
        }

        fun redirectToLogin(url: String, callback: Callback<ResponseBody>){
            val call = APIGenerator.createService(AccountInterface::class.java).redirectToLogin(url)
            call.enqueue(callback)
        }
    }
}