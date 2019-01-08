package com.example.q.cs496_2.activities

import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.q.cs496_2.R
import com.example.q.cs496_2.adapters.ImageSwipeAdapter
import com.example.q.cs496_2.asynctasks.UploadAsyncTask
import com.example.q.cs496_2.managers.ImageManager
import com.facebook.CallbackManager
import com.facebook.share.ShareApi
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.activity_image_detail.*
import java.io.File

class ImageDetailActivity : AppCompatActivity() {
    var position = 0
    // private var gestureDetector: GestureDetector? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requestWindowFeature(Window.FEATURE_NO_TITLE)
        //supportActionBar!!.hide() actionbar 없애는 라인

        // gestureDetector = GestureDetector(this@ImageDetailActivity, GestureListener())
        setContentView(R.layout.activity_image_detail)

        val extras= intent.extras
        position = extras.getInt("position")

        var imageSwipeAdapter = ImageSwipeAdapter(supportFragmentManager, position)
        imageSwipeAdapter.addFragment()
        imageViewPager.adapter = imageSwipeAdapter
    }
}