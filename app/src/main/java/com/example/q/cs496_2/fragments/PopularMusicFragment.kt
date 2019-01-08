package com.example.q.cs496_2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.q.cs496_2.R
import com.example.q.cs496_2.adapters.MusicListAdapter
import com.example.q.cs496_2.models.Music

class PopularMusicFragment : androidx.fragment.app.Fragment() {
    private var adapter: MusicListAdapter? = null
    private var musicList = ArrayList<Music>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_popular_music, container, false)
        return view
    }
}