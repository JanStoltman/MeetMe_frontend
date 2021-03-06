package com.yggdralisk.meetme.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.yggdralisk.meetme.MyApplication
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.api.MyCallback
import com.yggdralisk.meetme.api.calls.UsersCalls
import com.yggdralisk.meetme.api.models.UserModel
import kotlinx.android.synthetic.main.activity_login_main.*
import retrofit2.Call
import retrofit2.Response
import android.provider.SyncStateContract.Helpers.update
import android.content.pm.PackageInfo
import android.util.Base64
import android.util.Log
import com.yggdralisk.meetme.utility.FBProfileDataHelper
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : AppCompatActivity() {
    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val EMAIL: String = "email"
    }

    private val callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_main)
        loginButton.setReadPermissions(listOf(EMAIL))

        if (checkFacebookToken()) {
            getId()
        } else {
            setupLoginBUtton()
        }

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }

    private fun setupLoginBUtton() {
        loginButton.visibility = View.VISIBLE
        loadingSpinner.visibility = View.INVISIBLE
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                loginButton.visibility = View.INVISIBLE
                loadingSpinner.visibility = View.VISIBLE

                registerUser()
            }

            override fun onCancel() {
                Toast.makeText(applicationContext, applicationContext.getString(R.string.provider_cancel), Toast.LENGTH_LONG).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(applicationContext, applicationContext.getString(R.string.error_occured), Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun askForPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        } else {
            proceedToMapOrDataFill()
        }
    }

    private fun proceedToMapOrDataFill() {
        val user = MyApplication.currentUser

        if(user?.name.isNullOrBlank() || user?.surname.isNullOrBlank() || user?.email.isNullOrBlank()){
            startUserDataFillActivity()
        }
        else{
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

    private fun registerUser() {
        if (AccessToken.getCurrentAccessToken() != null) {
            //Toast.makeText(this, "Token: ${AccessToken.getCurrentAccessToken().userId}", Toast.LENGTH_LONG).show()
            UsersCalls.registerUser(hashMapOf(Pair("Token", AccessToken.getCurrentAccessToken().userId)),
                    object : MyCallback<Int>(this) {
                        override fun onResponse(call: Call<Int>?, response: Response<Int>?) {
                            super.onResponse(call, response)
                            getId()
                        }
                    })
        }
    }

    private fun getId() {
        UsersCalls.getMyId(object : MyCallback<Int>(this) {
            override fun onResponse(call: Call<Int>?, response: Response<Int>?) {
                super.onResponse(call, response)
                if(response?.isSuccessful == false){
                    registerUser()
                }else {
                    MyApplication.userId = response?.body() ?: 0
                    getUser(MyApplication.userId)
                }
            }
        })
    }


    private fun startUserDataFillActivity() {
        val intent = Intent(this, UserDataFillActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun getUser(id: Int) {
        UsersCalls.getUserById(id, object : MyCallback<UserModel>(this) {
            override fun onResponse(call: Call<UserModel>?, response: Response<UserModel>?) {
                super.onResponse(call, response)
                MyApplication.currentUser = response?.body()
                askForPermissions()
            }
        })
    }


    /**
     * Function which checks if there is any user currently logged in
     * Then checks if the stored token is still valid
     *
     * @return true if there is a user logged in and token is still valid, false otherwise
     */
    private fun checkFacebookToken(): Boolean {
        return AccessToken.getCurrentAccessToken() != null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                proceedToMapOrDataFill()
                return
            }

            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }
}
