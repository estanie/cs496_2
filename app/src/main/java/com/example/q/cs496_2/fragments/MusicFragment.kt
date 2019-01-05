package com.example.q.cs496_2.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.q.cs496_2.R
import com.example.q.cs496_2.activities.MyMusicEditActivity
import kotlinx.android.synthetic.main.fragment_music.view.*

class MusicFragment : androidx.fragment.app.Fragment() {
    // 여기서는 내가 만든 음악 리스트를 보여주고, 아무것도 없으면 오른쪽 하단 플러스 버튼에서 새로만들기
    // 음악 리스트 보여주고, 편집할 수 있는 창으로 넘어가주기.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_music, container, false)
        // getMy Music & from server too.
        view.newMusicButton.setOnClickListener {
            val intent = Intent(context, MyMusicEditActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}