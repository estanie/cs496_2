package com.example.q.cs496_2.fragments

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.q.cs496_2.R
import com.example.q.cs496_2.asynctasks.UploadAsyncTask
import com.example.q.cs496_2.dialogs.facebookLoginDialog
import com.example.q.cs496_2.managers.ImageManager
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareButton
import com.facebook.share.widget.ShareDialog
import kotlinx.android.synthetic.main.fragment_image.*
import kotlinx.android.synthetic.main.fragment_image.view.*
import java.io.File
import java.io.ObjectInput

class ImageFragment: Fragment() {
    var callbackManager : CallbackManager? = null
    private var isLoggedIn : Boolean = false
    var position = 0
    //var check_position :Int? =null

    private lateinit var fb_share_button : ShareButton

    fun newInstance(pos: Int): Fragment {
        val args = Bundle()
        args.putInt("position", pos)
        val fragment = ImageFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_image, container, false)
        setHasOptionsMenu(true)
        val pos = arguments!!.getInt("position")
        var accessToken = AccessToken.getCurrentAccessToken()
        isLoggedIn = accessToken != null && !accessToken.isExpired()
        position = pos
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

        fb_share_button = view.findViewById(R.id.fb_share_button) as ShareButton
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater){
        super.onCreateOptionsMenu(menu, menuInflater)
        menuInflater.inflate(R.menu.image_fragment_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean{
        super.onOptionsItemSelected(item)
        var isLoggedIn : Boolean = false
        var IS_LOGIN = 1004
        when (item.itemId) {
            //todo : menu button클릭시 facebook upload기능 및 데이터base에 추가
            R.id.action_backup_image -> {
                var accessToken = AccessToken.getCurrentAccessToken()
                isLoggedIn = accessToken != null && !accessToken.isExpired()
                if (!isLoggedIn) {
                    val fragment = SettingFragment()
                    fragment.setTargetFragment(this, IS_LOGIN)
                    fragmentManager!!.beginTransaction().run {
                        add(R.id.mainLayout, fragment)
                        addToBackStack(null)
                        commit()
                    }
                }
                else {
                    UploadAsyncTask(context!!, view!!.loginFacebookButton, false).execute(position.toString())
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun sharePhotoToFacebook(pos: Int) {
        var check : String = pos.toString()
        Log.d("%%%%", check)

        //var shareDialog : ShareDialog? =null
        var imgFile = File(ImageManager.getImage(pos).path)
        var content: SharePhotoContent? = null
        if (imgFile.exists()) {
            var myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            var photo = SharePhoto.Builder().setBitmap(myBitmap).build()
            content = SharePhotoContent.Builder().addPhoto(photo).build()
            Log.d("%%", content.toString())
        }
        //shareDialog!!.show(content)
        fb_share_button.setShareContent(content)
    }

    private fun loadImage(view: ImageView, path:String) {
        Glide.with(this)
            .load(File(path))
            .into(view)
    }
}