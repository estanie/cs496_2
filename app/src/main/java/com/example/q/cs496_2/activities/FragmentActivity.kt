package com.example.q.cs496_2.activities

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.q.cs496_2.R
import com.example.q.cs496_2.adapters.MyPagerAdapter
import com.example.q.cs496_2.fragments.MusicCutFragment
import kotlinx.android.synthetic.main.activity_fragment.*

class FragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fragment)

        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabsMain.setupWithViewPager(viewPager)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            Log.e("KEY_EVENT", "KEY!!!!!!")
            MusicCutFragment().onKeyDown(keyCode, event)
        }
        return super.onKeyDown(keyCode, event)
    }

}