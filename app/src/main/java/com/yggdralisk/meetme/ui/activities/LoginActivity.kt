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
import com.yggdralisk.meetme.R
import kotlinx.android.synthetic.main.activity_login_main.*


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

        if (checkToken()) {
            askForPermissions()
        } else {
            setupLoginBUtton()
        }

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }

    private fun setupLoginBUtton() {
        loginButton.setReadPermissions(listOf(
                "public_profile", "email"))
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
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
            proceedToMap()
        }
    }

    private fun proceedToMap() {
        startActivity(Intent(applicationContext, EventsActivity::class.java))
        this.finish()
    }

    private fun registerUser() {
        if (AccessToken.getCurrentAccessToken() != null) {

        }
    }

    /**
     * Function which checks if there is any user currently logged in
     * Then checks if the stored token is still valid
     *
     * @return true if there is a user logged in and token is still valid, false otherwise
     */
    private fun checkToken(): Boolean {
        return AccessToken.getCurrentAccessToken() != null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                proceedToMap()
                return
            }

            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }
}
