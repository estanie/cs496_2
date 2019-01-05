package com.example.q.cs496_2.activities

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.q.cs496_2.R
import com.example.q.cs496_2.adapters.MyPagerAdapter
import kotlinx.android.synthetic.main.activity_fragment.*

class FragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fragment)

        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabsMain.setupWithViewPager(viewPager)

    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.fragment_menu, menu)
        return true
    }
}