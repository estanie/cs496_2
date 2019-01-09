package com.example.q.cs496_2.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.q.cs496_2.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.login_dialog.*

class facebookLoginDialog(context: Context) : Dialog(context) {


    init {
        setContentView(R.layout.login_dialog)
        setTitle(R.string.alert_title_success)
        var callbackManager = CallbackManager.Factory.create()
        var facebookCallback = object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Log.e("ONSUCCESS", "SUCCESS!")
                // 유저 등록하기
                dismiss()
            }
            override fun onCancel() {
                dismiss()
            }
            override fun onError(error: FacebookException?) {
                dismiss()
            }
        }
        LoginManager.getInstance().registerCallback(callbackManager, facebookCallback)
        facebookLoginButton.setReadPermissions("email")

        facebookLoginButton.setOnClickListener {
            dismiss()
        }
        (findViewById<View>(R.id.button_do_nothing) as Button)
            .setOnClickListener { dismiss() }
    }

    class UserTask()
}
