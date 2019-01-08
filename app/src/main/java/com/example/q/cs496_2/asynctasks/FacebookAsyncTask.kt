package com.example.q.cs496_2.asynctasks

import android.content.Context
import android.os.AsyncTask
import com.facebook.AccessToken
import com.facebook.login.widget.LoginButton

class FacebookAsyncTask(): AsyncTask<String, String, String>() {
    var url = ""
    var mContext: Context? = null
    private val CONNECTION_TIMEOUT = 60000
    private var accessToken = AccessToken.getCurrentAccessToken()
    private var isLoggedIn = accessToken != null && !accessToken.isExpired()
    private var encoded :String? = null
    constructor(context: Context):this() {
        mContext = context
    }

    override fun doInBackground(vararg values: String?): String {

        // TODO(gayeon): 받고 나서 user등록해야함.
        return " "
    }
}