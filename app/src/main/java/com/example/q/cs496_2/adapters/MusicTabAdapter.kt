package com.example.q.cs496_2.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.q.cs496_2.R
import com.example.q.cs496_2.fragments.*
import kotlinx.android.synthetic.main.fragment_music.view.*

class MusicTabAdapter(fm: FragmentManager, context: Context) : FragmentPagerAdapter(fm) {
    private var mContext : Context? = null
    init {
        mContext = context
    }
    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> {
                PopularMusicFragment()

            }
            1 -> {
                MyMusicFragment()
            }
            else -> {
                LikedMusicFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    private lateinit var myDrawable : Drawable
    private lateinit var sb : SpannableStringBuilder
    private lateinit var span : ImageSpan
    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> {
                myDrawable = mContext!!.resources.getDrawable(R.drawable.ic_round_grade_24px)
                sb = SpannableStringBuilder(" ")
                myDrawable.setBounds(0, 0, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight())
                span = ImageSpan(myDrawable, ImageSpan.ALIGN_BASELINE)
                sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                return sb
            }
            1 -> {
                myDrawable = mContext!!.resources.getDrawable(R.drawable.ic_round_face_24px)
                sb = SpannableStringBuilder(" ")
                myDrawable.setBounds(0, 0, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight())
                span = ImageSpan(myDrawable, ImageSpan.ALIGN_BASELINE)
                sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                return sb
            }
            else -> {
                myDrawable = mContext!!.resources.getDrawable(R.drawable.ic_round_favorite_24px)
                sb = SpannableStringBuilder(" ")
                myDrawable.setBounds(0, 0, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight())
                span = ImageSpan(myDrawable, ImageSpan.ALIGN_BASELINE)
                sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                return sb
            }
        }
    }
}