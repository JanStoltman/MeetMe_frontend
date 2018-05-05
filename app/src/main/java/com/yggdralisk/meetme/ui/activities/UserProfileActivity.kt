package com.yggdralisk.meetme.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.nostra13.universalimageloader.core.ImageLoader
import com.yggdralisk.meetme.MockApplication
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.api.models.UserModel
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileActivity : AppCompatActivity() {
    companion object {
        const val USER_ID = "USER_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        val userId: Int = intent.getIntExtra(USER_ID, -1)
        val user: UserModel? = MockApplication.mockUsers.find { u -> u.id == userId }

        bindData(user)
    }

    private fun bindData(user: UserModel?) {
        if (!user?.photoImage.isNullOrBlank()) {
            ImageLoader.getInstance().displayImage(user?.photoImage, findViewById<ImageView>(R.id.imageView))
        }
        findViewById<TextView>(R.id.nameTextView)?.setText(user?.name)
        findViewById<TextView>(R.id.surnameTextView)?.setText(user?.surname)
        findViewById<TextView>(R.id.emailTextView)?.setText(user?.email)
        findViewById<TextView>(R.id.phoneTextView)?.setText(user?.phoneNumber)
        //findViewById<TextView>(R.id.birthdateTextView)?.setText(user?.birthDay.toString())
        findViewById<TextView>(R.id.bioTextView)?.setText(user?.bio)
        findViewById<TextView>(R.id.userRating).setText(String.format("%.2f", user?.rating))
    }
}
