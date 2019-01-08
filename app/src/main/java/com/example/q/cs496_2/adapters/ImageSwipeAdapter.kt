package com.example.q.cs496_2.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.q.cs496_2.fragments.ImageFragment
import com.example.q.cs496_2.managers.ImageManager

class ImageSwipeAdapter(fm: FragmentManager, position: Int): FragmentPagerAdapter(fm) {
    var position = position
    private var fragmentList = ArrayList<Fragment>()

    override fun getItem(position:Int): Fragment {
        return fragmentList.get(position)
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addFragment() {

        for (i in 0..ImageManager.getSize()-1) {
            fragmentList.add(ImageFragment().newInstance(i))
        }

    }

}