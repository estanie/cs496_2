package com.example.q.cs496_2.fragments

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.q.cs496_2.R
import com.example.q.cs496_2.managers.ImageManager
import kotlinx.android.synthetic.main.fragment_image.view.*
import java.io.File

class ImageFragment: Fragment() {

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
        Log.e("MY FRAGMENT PATH", ""+pos)
        loadImage(view.imageDetail, ImageManager.getImage(pos).path!!)
        return view
    }

    private fun loadImage(view: ImageView, path:String) {

        Glide.with(this)
            .load(File(path))
            .into(view)
    }
}