package com.example.q.cs496_2.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.q.cs496_2.fragments.AddressFragment
import com.example.q.cs496_2.fragments.GalleryFragment
import com.example.q.cs496_2.fragments.MusicFragment

class MyPagerAdapter(fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return when(position) {
            0 -> {
                AddressFragment()
            }
            1 -> {
                GalleryFragment()
            }
            else -> {
                return MusicFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "PhoneBook"
            1 -> "Gallery"
            else -> return "music"
        }
    }
}