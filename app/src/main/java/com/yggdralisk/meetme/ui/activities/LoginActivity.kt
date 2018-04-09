package com.yggdralisk.meetme.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.api.APIGenerator
import com.yggdralisk.meetme.api.MyCallback
import com.yggdralisk.meetme.api.calls.AccountCalls
import com.yggdralisk.meetme.api.models.ExternalLogin
import kotlinx.android.synthetic.main.activity_login_main.*
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_main)
        askForPermissions()
        if (checkToken() || true) { //TODO remove true once gearhost is up
            askForPermissions()
        } else {
            getAccountProviders()
        }
    }

    private fun askForPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }else{
            proceedToMap()
        }
    }

    private fun getAccountProviders() {
        AccountCalls
                .getPossibleExternalLogins(object : MyCallback<List<ExternalLogin>>(baseContext) {
                    override fun onResponse(call: Call<List<ExternalLogin>>?, response: Response<List<ExternalLogin>>?) {
                        if (response != null && response.isSuccessful) {
                            displayPossibleAccounts(response.body())
                        } else if (response == null) {
                            Toast.makeText(baseContext, R.string.no_internet_alert, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(baseContext, response.message(), Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    private fun displayPossibleAccounts(responses: List<ExternalLogin>?) {
        loadingSpinner.visibility = View.GONE
        if (responses != null) {
            for(response in responses) {
                val b = Button(baseContext)
                b.text = response.name
                b.setOnClickListener {
                    redirectToProvider(response.url)
                }

                mainLayout.addView(b)
            }
        }
    }

    private fun redirectToProvider(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(APIGenerator.BASE_URL + url)))
    }

    private fun proceedToMap() {
        startActivity(Intent(applicationContext, EventsActivity::class.java))
        this.finish()
    }

    /**
     * Function which checks if there is any user currently logged in
     * Then checks if the stored token is still valid
     *
     * @return true if there is a user logged in and token is still valid, false otherwise
     */
    fun checkToken(): Boolean {
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    proceedToMap()
                }
                return
            }

            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }
}
