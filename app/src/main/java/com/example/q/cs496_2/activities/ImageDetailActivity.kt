package com.example.q.cs496_2.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.q.cs496_2.R
import com.example.q.cs496_2.adapters.ImageSwipeAdapter
import kotlinx.android.synthetic.main.activity_image_detail.*

class ImageDetailActivity : AppCompatActivity() {
    var position = 0
    // private var gestureDetector: GestureDetector? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        val extras= intent.extras
        position = extras.getInt("position")

        var imageSwipeAdapter = ImageSwipeAdapter(supportFragmentManager, position)
        imageSwipeAdapter.addFragment()
        imageViewPager.adapter = imageSwipeAdapter
        imageViewPager.currentItem = position
    }
}