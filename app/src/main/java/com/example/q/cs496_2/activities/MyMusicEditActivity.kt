package com.example.q.cs496_2.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.q.cs496_2.R

class MyMusicEditActivity :AppCompatActivity() {
    var position = -1
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_edit_music)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()

        val extras = intent.extras
        position = extras.getInt("position", -1)

        if (position!= -1) { } // 기존 정보 불러와서 채우기.
    }
}