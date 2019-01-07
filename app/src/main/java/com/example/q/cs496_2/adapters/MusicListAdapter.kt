package com.example.q.cs496_2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.q.cs496_2.R
import com.example.q.cs496_2.models.Music
import kotlinx.android.synthetic.main.entry_music.view.*

class MusicListAdapter (val context: Context, val musicList: ArrayList<Music>) : RecyclerView.Adapter<MusicListAdapter.Holder>() {
    private var mMusicList = musicList
    public fun add(music: Music) {
        mMusicList.add(music)
    }

    override fun getItemCount(): Int {
        return mMusicList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Holder {
        val holder = Holder(LayoutInflater.from(context).inflate(R.layout.entry_music, parent, false))
        return holder
    }

    override fun onBindViewHolder(holder: MusicListAdapter.Holder, position: Int) {
        holder.bind(mMusicList[position], context)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        fun bind (music: Music, context: Context) {
            itemView.name.text = music.name
            itemView.author.text = music.author
        }
    }
}