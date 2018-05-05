package com.yggdralisk.meetme.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.nostra13.universalimageloader.core.ImageLoader
import com.yggdralisk.meetme.MockApplication
import com.yggdralisk.meetme.R

/**
 * Created by Jan Stoltman on 4/8/18.
 */
class UserProfileFragment : Fragment() {

    companion object {
        fun newInstance(): UserProfileFragment {
            return UserProfileFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater?.inflate(R.layout.user_profile_fragment, container, false)
        readUserData(view)
        view?.findViewById<Button>(R.id.logoutButton)?.setOnClickListener({
            disconnectFromFacebook()
        })
        return view
    }
    private fun disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            this.activity.finish()
            return // already logged out
        }

        GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null,
                HttpMethod.DELETE, GraphRequest.Callback {
            LoginManager.getInstance().logOut()
            this.activity.finish()
        }).executeAsync()
    }

    private fun saveUserData() {
        val user = MockApplication.mockUsers[0]
        user.name = view?.findViewById<EditText>(R.id.nameTextView)?.text.toString()
        user.surname = view?.findViewById<EditText>(R.id.surnameTextView)?.text.toString()
        user.email = view?.findViewById<EditText>(R.id.emailTextView)?.text.toString()
        user.phoneNumber = view?.findViewById<EditText>(R.id.phoneTextView)?.text.toString()
        //user.birthDay = view?.findViewById<EditText>(R.id.birthdateTextView)?.text.toString().toLong()
        user.bio = view?.findViewById<EditText>(R.id.bioTextView)?.text.toString()

        MockApplication.mockUsers[0] = user
        MockApplication.saveUsers(context) //TODO: add data validation
        Toast.makeText(context, getString(R.string.data_saved), Toast.LENGTH_SHORT).show()
    }

    private fun readUserData(view: View?) {
        val user = MockApplication.mockUsers[0]
        ImageLoader.getInstance().displayImage(user.photoImage, view?.findViewById<ImageView>(R.id.imageView))
        view?.findViewById<EditText>(R.id.nameTextView)?.setText(user.name)
        view?.findViewById<EditText>(R.id.surnameTextView)?.setText(user.surname)
        view?.findViewById<EditText>(R.id.emailTextView)?.setText(user.email)
        view?.findViewById<EditText>(R.id.phoneTextView)?.setText(user.phoneNumber)
        // view?.findViewById<EditText>(R.id.birthdateTextView)?.setText(user.birthDay.toString())
        view?.findViewById<EditText>(R.id.bioTextView)?.setText(user.bio)
        view?.findViewById<TextView>(R.id.userRating)?.text = String.format("%.2f", user.rating)

        view?.findViewById<Button>(R.id.saveButton)?.setOnClickListener { saveUserData() }
    }
}