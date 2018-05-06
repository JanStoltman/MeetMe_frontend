package com.yggdralisk.meetme.api.calls

import com.yggdralisk.meetme.api.APIGenerator
import com.yggdralisk.meetme.api.interfaces.UsersInterface
import com.yggdralisk.meetme.api.models.RegisterModel
import com.yggdralisk.meetme.api.models.SimpleUserModel
import com.yggdralisk.meetme.api.models.UserModel
import okhttp3.ResponseBody
import retrofit2.Callback

/**
 * Created by Jan Stoltman on 4/7/18.
 */
class UsersCalls {
    companion object {
        fun registerUser(body: HashMap<String, Any>, callback: Callback<Int>) {
            val call = APIGenerator.createService(UsersInterface::class.java)
                    .registerUser(body)
            call.enqueue(callback)
        }

        fun getMyId(callback: Callback<Int>) {
            val call = APIGenerator.createService(UsersInterface::class.java)
                    .getMyId()
            call.enqueue(callback)
        }

        fun getUsers (callback: Callback<List<UserModel>>){
            val call = APIGenerator.createService(UsersInterface::class.java)
                    .getUsers()
            call.enqueue(callback)
        }

        fun getUserById(id: Int, callback: Callback<UserModel>){
            val call = APIGenerator.createService(UsersInterface::class.java)
                    .getUserById(id)
            call.enqueue(callback)
        }

        fun updateMyData(body: UserModel, callback: Callback<ResponseBody>){
            val call = APIGenerator.createService(UsersInterface::class.java)
                    .updateMyData(body)
            call.enqueue(callback)
        }

        fun getNamesForIds(body: List<Int>, callback: Callback<List<SimpleUserModel>>){
            val call = APIGenerator.createService(UsersInterface::class.java)
                    .getUsersNameFromIds(body)
            call.enqueue(callback)
        }
    }
}