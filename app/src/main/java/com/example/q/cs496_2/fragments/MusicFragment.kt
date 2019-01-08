package com.example.q.cs496_2.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.q.cs496_2.R
import com.example.q.cs496_2.adapters.MusicListAdapter
import com.example.q.cs496_2.adapters.MusicTabAdapter
import com.example.q.cs496_2.adapters.MyPagerAdapter
import com.example.q.cs496_2.models.Music
import com.example.q.cs496_2.music.SongMetadataReader
import kotlinx.android.synthetic.main.activity_fragment.*
import kotlinx.android.synthetic.main.fragment_music.view.*
import java.io.File

class MusicFragment : androidx.fragment.app.Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_music, container, false)
        // get My MusicId from server too.
        val fragmentAdapter = MusicTabAdapter(activity!!.supportFragmentManager, view.context)
        view.musicViewPager.adapter = fragmentAdapter

        view.musicTab.setupWithViewPager(view.musicViewPager)
        view.musicViewPager.setOnTouchListener { v, event ->
            true
        }
        // 스와이프 못하게 하기!
        return view
    }

    override fun onStart() {
        super.onStart()


    }
}