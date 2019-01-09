package com.example.q.cs496_2.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.q.cs496_2.R
import com.example.q.cs496_2.managers.Facebook
import com.facebook.AccessToken
import com.facebook.CallbackManager
import kotlinx.android.synthetic.main.fragment_setting.view.*

class SettingFragment : androidx.fragment.app.Fragment() {
    var callbackManager : CallbackManager? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        callbackManager = CallbackManager.Factory.create()
        view.setBackgroundColor(Color.WHITE)
        view.facebookLoginButton.setReadPermissions("email")
        view.facebookLoginButton.setFragment(this)
        view.facebookLoginButton.setOnClickListener {
        }
        view.gobackbutton.setOnClickListener{
            fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        Facebook.accessToken = AccessToken.getCurrentAccessToken()
        Facebook.isLoggedIn = Facebook.accessToken != null && !(Facebook.accessToken)!!.isExpired()
        super.onActivityResult(requestCode, resultCode, data)
        fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}