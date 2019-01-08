package com.example.q.cs496_2.adapters

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.q.cs496_2.R
import com.example.q.cs496_2.models.Music
import com.facebook.login.widget.LoginButton
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
            itemView.musicPlayButton.setOnClickListener {
                Toast.makeText(context, "음악을 재생합니다.", Toast.LENGTH_LONG).show()
                var url = "http://socrip4.kaist.ac.kr:3380/api/music/stream/"+music.id
                Log.e("URL", url)
                var mPlayer = MediaPlayer()
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                if (mPlayer.isPlaying()) {
                    mPlayer.stop()
                    mPlayer.reset()
                    mPlayer.release()
                }
                try {
                    mPlayer.setDataSource(url)
                    mPlayer.prepare()
                    mPlayer.start()
                } catch(e: Exception) {
                    e.printStackTrace()
                }
                mPlayer.setOnCompletionListener {
                }
                // StreamTask(context, music.id).execute()
            }

        }
    }
     class StreamTask(): AsyncTask<String, String, String>() {
         var musicId = ""
         var mContext :Context? = null
         constructor(context: Context, id: String):this() {
             mContext = context
             musicId = id
         }

        override fun doInBackground(vararg params: String?): String {
            // var
            return " "
        }
    }
}