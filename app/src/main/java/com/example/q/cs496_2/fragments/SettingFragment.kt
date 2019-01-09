package com.example.q.cs496_2.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.android.volley.Request
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.q.cs496_2.R
import com.example.q.cs496_2.managers.Facebook
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_setting.view.*

class SettingFragment : androidx.fragment.app.Fragment() {
    var callbackManager : CallbackManager? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        callbackManager = CallbackManager.Factory.create()
        view.setBackgroundColor(Color.WHITE)
        view.facebookLoginButton.setReadPermissions("email")
        view.facebookLoginButton.setFragment(this)
        var facebookCallback = object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
            }
            override fun onCancel() {}
            override fun onError(error: FacebookException?) {}
        }
        LoginManager.getInstance().registerCallback(callbackManager, facebookCallback)
        view.facebookLoginButton.setOnClickListener {
        }
        view.gobackbutton.setOnClickListener{
            fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        Facebook.accessToken = AccessToken.getCurrentAccessToken()
        Facebook.isLoggedIn = Facebook.accessToken != null && !(Facebook.accessToken)!!.isExpired()
        if (Facebook.accessToken!= null)
            UserRegistTask(context!!, Facebook.accessToken!!.userId.toString()).execute()
        fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    class UserRegistTask() : AsyncTask<String, String, String>() {
        var mContext : Context? = null
        var facebookId = ""
        constructor(context: Context, id: String) :this() {
            mContext = context
            facebookId = id
        }
        override fun doInBackground(vararg params: String?): String {
            var url = "http://socrip4.kaist.ac.kr:3380/api/user"
            val mRequestQueue = Volley.newRequestQueue(mContext)
            val mStringRequest = object: StringRequest(Request.Method.POST, url, { s ->}, { e->}) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>(1)
                    var gson = Gson()
                    var likeInfo = HashMap<String, String>(1)
                    likeInfo["facebook_id"] = facebookId
                    likeInfo["name"] = "Anonymous"
                    var json = gson.toJson(likeInfo)
                    Log.e("JSON", json)
                    params["likeInfo"] = json
                    return params
                }
            }

            mStringRequest.retryPolicy = object : RetryPolicy {
                override fun getCurrentTimeout() = 15000
                override fun getCurrentRetryCount() = 0
                override fun retry(error: VolleyError) = Unit
            }
            mRequestQueue.add(mStringRequest)

            return " "
        }
    }
}