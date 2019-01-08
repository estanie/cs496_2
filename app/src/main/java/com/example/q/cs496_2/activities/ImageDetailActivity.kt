package com.example.q.cs496_2.activities

import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.q.cs496_2.R
import com.example.q.cs496_2.adapters.ImageSwipeAdapter
import com.facebook.CallbackManager
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.activity_image_detail.*

class ImageDetailActivity : AppCompatActivity() {
    var position = 0
    // private var gestureDetector: GestureDetector? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // gestureDetector = GestureDetector(this@ImageDetailActivity, GestureListener())
        setContentView(R.layout.activity_image_detail)

        val extras= intent.extras
        position = extras.getInt("position")

        var imageSwipeAdapter = ImageSwipeAdapter(supportFragmentManager, position)
        imageSwipeAdapter.addFragment()
        imageViewPager.adapter = imageSwipeAdapter
    }
}