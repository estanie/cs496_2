package com.example.q.cs496_2.adapters

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.q.cs496_2.R
import com.example.q.cs496_2.models.Music
import com.facebook.AccessToken
import com.facebook.login.widget.LoginButton
import com.google.gson.Gson
import kotlinx.android.synthetic.main.entry_music.view.*

class MusicListAdapter(val context: Context, val musicList: ArrayList<Music>) :
    RecyclerView.Adapter<MusicListAdapter.Holder>() {
    var mMusicList = musicList
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
        holder.bind(mMusicList[position], context, position)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        fun bind(music: Music, context: Context, position: Int) {
            var mPlayer = MediaPlayer()
            itemView.name.text = music.name
            itemView.author.text = music.author
            if (music.liked) {
                itemView.likeButton.setImageResource(R.drawable.ic_round_favorite_24px)
            } else {
                itemView.likeButton.setImageResource(R.drawable.ic_round_favorite_border_24px)
            }
            itemView.likeButton.setOnClickListener {

                if (!music.liked) {
                    itemView.likeButton.setImageResource(R.drawable.ic_round_favorite_24px)
                    Toast.makeText(context, "이 음악을 좋아합니다.", Toast.LENGTH_LONG).show()
                } else {
                    itemView.likeButton.setImageResource(R.drawable.ic_round_favorite_border_24px)
                }
                LikeTask(context, music.id, !music.liked).execute()
                var tMusic = Music(music.id, music.name, music.author, !music.liked, music.path)
                mMusicList[position] = tMusic

            }
            itemView.musicPlayButton.setOnClickListener {
                var url = "http://socrip4.kaist.ac.kr:3380/api/music/stream/" + music.id
                Log.e("URL", url)
                try {
                    if (mPlayer.isPlaying) {
                        mPlayer.stop()
                        mPlayer.reset()
                        itemView.musicPlayButton.setImageResource(R.drawable.ic_round_play_circle_outline_24px)
                    } else {
                        Toast.makeText(context, "음악을 재생합니다.", Toast.LENGTH_LONG).show()
                        mPlayer.setDataSource(url)
                        mPlayer.prepare()
                        mPlayer.start()
                        itemView.musicPlayButton.setImageResource(R.drawable.ic_round_pause_circle_outline_24px)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                mPlayer.setOnCompletionListener {
                    itemView.musicPlayButton.setImageResource(R.drawable.ic_round_play_circle_outline_24px)
                    mPlayer.reset()
                }
                // StreamTask(context, music.id).execute()
            }

        }
    }

    // 좋아요
    class LikeTask() : AsyncTask<String, String, String>() {
        private var accessToken = AccessToken.getCurrentAccessToken()
        private var isLoggedIn = accessToken != null && !accessToken.isExpired()
        var musicId = ""
        var mContext: Context? = null
        var mLike = false
        var url = ""

        constructor(context: Context, id: String, like: Boolean) : this() {
            mContext = context
            musicId = id
            mLike = like
        }

        override fun doInBackground(vararg params: String?): String {
            if (mLike) {
                url = "http://socrip4.kaist.ac.kr:3380/api/music/like/"

            } else {
                url = "http://socrip4.kaist.ac.kr:3380/api/music/like/cancel"
            }
            val mRequestQueue = Volley.newRequestQueue(mContext)
            val mStringRequest = object: StringRequest(Request.Method.POST, url, {s ->}, {e->}) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>(1)
                    var gson = Gson()
                    var likeInfo = HashMap<String, String>(1)
                    likeInfo["facebook_id"] = accessToken.userId.toString()
                    likeInfo["music_id"] = musicId
                    var json = gson.toJson(likeInfo)
                    Log.e("JSON", json)
                    params["likeInfo"] = json
                    return params
                }
            }

            mStringRequest.retryPolicy = object : RetryPolicy {
                override fun getCurrentTimeout() = 15000
                override fun getCurrentRetryCount() = 0
                override fun retry(error: VolleyError) = Unit
            }
            mRequestQueue.add(mStringRequest)

            return " "
        }
    }
}