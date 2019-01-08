package com.example.q.cs496_2.fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.q.cs496_2.R
import com.example.q.cs496_2.asynctasks.UploadAsyncTask
import com.example.q.cs496_2.managers.ImageManager
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.fragment_image.view.*
import java.io.File

class ImageFragment: Fragment() {
    var callbackManager : CallbackManager? = null
    private var isLoggedIn : Boolean = false

    fun newInstance(pos: Int): Fragment {
        val args = Bundle()
        args.putInt("position", pos)
        val fragment = ImageFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_image, container, false)
        val pos = arguments!!.getInt("position")

        var accessToken = AccessToken.getCurrentAccessToken()
        isLoggedIn = accessToken != null && !accessToken.isExpired()

        loadImage(view.imageDetail, ImageManager.getImage(pos).path!!)
        view.loginFacebookButton.setFragment(this)
        view.loginFacebookButton.setReadPermissions("email")
        view.loginFacebookButton.setOnClickListener {
            callbackManager = CallbackManager.Factory.create()
            var facebookCallback = object: FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    accessToken = AccessToken.getCurrentAccessToken()
                    isLoggedIn = accessToken != null && !accessToken.isExpired()
                }
                override fun onCancel() {}
                override fun onError(error: FacebookException?) {}
            }
            view.loginFacebookButton.registerCallback(callbackManager, facebookCallback)
        }
        view.uploadFab.setOnClickListener {
            if (!isLoggedIn) {
                view.loginFacebookButton.callOnClick()
            }
            UploadAsyncTask(context!!, view.loginFacebookButton, false).execute(pos.toString())
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        menuInflater.inflate(R.menu.fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //todo : menu button클릭시 facebook upload기능 및 데이터base에 추가
            R.id.action_backup_image -> {
                uploadAllImage()
                return true
            }
            R.id.action_facebook_upload -> {

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun uploadAllImage() {
        UploadAsyncTask(context!!, null, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0.toString())
    }


    private fun loadImage(view: ImageView, path:String) {
        Glide.with(this)
            .load(File(path))
            .into(view)
    }


}