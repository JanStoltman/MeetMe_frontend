package com.yggdralisk.meetme.ui.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.nostra13.universalimageloader.core.ImageLoader
import com.yggdralisk.meetme.MyApplication
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.api.MyCallback
import com.yggdralisk.meetme.api.calls.UsersCalls
import com.yggdralisk.meetme.api.models.UserModel
import com.yggdralisk.meetme.utility.FBProfileDataHelper
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class UserDataFillActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_data_fill)
        val facebookData = intent.extras
        mergeUserDataWithFacebookData(facebookData)

        readUserData()

//        val user = MyApplication.currentUser
//        //TODO check if user has all obligatory fields filled, if not - get data from facebook
//        if(user?.name == null || user?.surname == null || user?.email == null){
//            Toast.makeText(this, "Fill missing user data", Toast.LENGTH_LONG).show()
//            mergeUserDataWithFacebookData()
//        }

        setListeners()
    }

    private fun setListeners() {
        findViewById<Button>(R.id.logoutButton)?.setOnClickListener({
            disconnectFromFacebook()
        })
        findViewById<Button>(R.id.saveButton)?.setOnClickListener { saveUserData() }
    }

    private fun disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            finish()
            finishAffinity()
            return // already logged out
        }

        GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null,
                HttpMethod.DELETE, GraphRequest.Callback {
            LoginManager.getInstance().logOut()
            finish()
        }).executeAsync()
        finishAffinity()
    }

    private fun readUserData() {
        val user = MyApplication.currentUser
        ImageLoader.getInstance().displayImage(user?.photoImage, findViewById<ImageView>(R.id.imageView))
        findViewById<EditText>(R.id.nameTextView)?.setText(user?.name)
        findViewById<EditText>(R.id.surnameTextView)?.setText(user?.surname)
        findViewById<EditText>(R.id.emailTextView)?.setText(user?.email)
        findViewById<EditText>(R.id.phoneTextView)?.setText(user?.phoneNumber)
        //findViewById<EditText>(R.id.birthdateTextView)?.setText(user.birthDay.toString())
        findViewById<EditText>(R.id.bioTextView)?.setText(user?.bio)
        findViewById<TextView>(R.id.userRating)?.setText(String.format("%.2f", user?.rating))
    }

    private fun saveUserData() {
        MyApplication.currentUser?.let {
            val user = MyApplication.currentUser
            user?.name = findViewById<EditText>(R.id.nameTextView)?.text.toString()
            user?.surname = findViewById<EditText>(R.id.surnameTextView)?.text.toString()
            user?.email = findViewById<EditText>(R.id.emailTextView)?.text.toString()
            user?.phoneNumber = findViewById<EditText>(R.id.phoneTextView)?.text.toString()
            //user.birthDay = view?.findViewById<EditText>(R.id.birthdateTextView)?.text.toString().toLong()
            user?.bio = findViewById<EditText>(R.id.bioTextView)?.text.toString()

            //TODO
            //verifyUserInput()

            //TODO spinner

            UsersCalls.updateMyData(user!!, object : MyCallback<ResponseBody>(this) {
                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    super.onResponse(call, response)
                    Toast.makeText(applicationContext, getString(R.string.data_saved), Toast.LENGTH_SHORT).show()
                    startMainActivity()
                }
            })
        }
    }

    private fun startMainActivity() {
        val toMainIntent = Intent(this, MainActivity::class.java)
        toMainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(toMainIntent)
    }

    private fun mergeUserDataWithFacebookData(facebookData : Bundle){
        val user = MyApplication.currentUser

        if (facebookData.containsKey("id")) {
            val facebookId = facebookData["id"] as? String //TODO unused
        }

        if (facebookData.containsKey("first_name")){
            user?.name = user?.name ?: facebookData["first_name"] as? String
        }
        if (facebookData.containsKey("last_name")){
            user?.surname = user?.surname ?: facebookData["last_name"] as? String
        }
        if (facebookData.containsKey("email")){
            user?.email = user?.email ?: facebookData["email"] as? String
        }

        user?.photoImage = user?.photoImage ?: "https://graph.facebook.com/" + facebookData["id"] + "/picture?type=large"
    }
}
