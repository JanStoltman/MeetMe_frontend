package com.yggdralisk.meetme.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.nostra13.universalimageloader.core.ImageLoader
import com.yggdralisk.meetme.MyApplication
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.api.MyCallback
import com.yggdralisk.meetme.api.calls.UsersCalls
import com.yggdralisk.meetme.api.models.UserModel
import retrofit2.Call
import retrofit2.Response
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ApplicationInfo
import android.net.Uri
import kotlinx.android.synthetic.main.activity_user_profile.*


class UserProfileActivity : AppCompatActivity() {
    companion object {
        const val USER_ID = "USER_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        val userId: Int = intent.getIntExtra(USER_ID, -1)
        getUser(userId)
    }

    private fun getUser(userId: Int) {
        UsersCalls.getUserById(userId, object: MyCallback<UserModel>(this){
            override fun onResponse(call: Call<UserModel>?, response: Response<UserModel>?) {
                super.onResponse(call, response)
                bindData(response?.body())
            }
        })
    }

    private fun bindData(user: UserModel?) {
        if (!user?.photoImage.isNullOrBlank()) {
            ImageLoader.getInstance().displayImage(user?.photoImage, findViewById<ImageView>(R.id.imageView))
        }
        findViewById<TextView>(R.id.nameTextView)?.text = user?.name
        findViewById<TextView>(R.id.surnameTextView)?.text = user?.surname
        findViewById<TextView>(R.id.emailTextView)?.text = user?.email
        findViewById<TextView>(R.id.phoneTextView)?.text = user?.phoneNumber
        //findViewById<TextView>(R.id.birthdateTextView)?.setText(user?.birthDay.toString())
        findViewById<TextView>(R.id.bioTextView)?.text = user?.bio
        findViewById<TextView>(R.id.userRating).setText(String.format("%.2f", user?.rating))
    }

}
