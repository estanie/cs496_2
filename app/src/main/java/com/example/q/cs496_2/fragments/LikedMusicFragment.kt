package com.example.q.cs496_2.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.q.cs496_2.R
import com.example.q.cs496_2.adapters.MusicListAdapter
import com.example.q.cs496_2.models.Music
import com.facebook.AccessToken
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_liked_music.view.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class LikedMusicFragment : androidx.fragment.app.Fragment() {
    private var adapter: MusicListAdapter? = null
    private var musicList= ArrayList<Music>()

    override fun onStart() {
        super.onStart()
        getDataTask(this).execute()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_liked_music, container, false)
        adapter = MusicListAdapter(context!!, musicList)
        view.favoriteList.adapter = adapter
        view.favoriteList.layoutManager = LinearLayoutManager(context)
        return view

    }

    fun updateAdapter(list: ArrayList<Music>) {
        musicList = list
        adapter = MusicListAdapter(context!!, musicList)
        view!!.favoriteList.adapter = adapter
        view!!.favoriteList.layoutManager = LinearLayoutManager(context)
        adapter!!.notifyDataSetChanged()
    }
    class getDataTask(): AsyncTask<String, String, String>() {

        private var accessToken = AccessToken.getCurrentAccessToken()
        private var isLoggedIn = accessToken != null && !accessToken.isExpired()
        private val CONNECTION_TIMEOUT = 60000
        private var mMusicList = ArrayList<Music>()
        var url = "http://socrip4.kaist.ac.kr:3380/api/music/like/"
        lateinit var mFragment : LikedMusicFragment
        constructor(fragment: LikedMusicFragment):this() {
            mFragment = fragment

        }

        override fun doInBackground(vararg values: String?): String {
            if (!isLoggedIn) {
                // Toast.makeText(mFragment.context, "로그인을 먼저 해주세요!", Toast.LENGTH_LONG).show()
                return " "
            }
            var urlConnection: HttpURLConnection? = null
            try {
                Log.e("URL", url+accessToken.userId.toString())
                var link = URL(url+accessToken.userId.toString())
                urlConnection = link.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = CONNECTION_TIMEOUT
                var inString = streamToString(urlConnection.inputStream)
                Log.e("INSTRING", inString)
                publishProgress(inString)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (urlConnection != null) urlConnection.disconnect()
            }
            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                val gson = GsonBuilder().create()
                mMusicList = gson.fromJson(values[0], object: TypeToken<List<Music>>() {}.type)
                Log.e("LIKEMUSIC2",""+mMusicList)
                mFragment.updateAdapter(mMusicList)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        override fun onPostExecute(result: String?) {

        }

        private fun streamToString(inputStream: InputStream): String {
            val bufferReader = BufferedReader(InputStreamReader(inputStream))
            var line: String
            var result = ""
            try {
                do {
                    line = bufferReader.readLine()
                    if (line != null) {
                        result+=line
                    }
                } while (line != null)
                inputStream.close()
            } catch(ex: Exception) {
                ex.printStackTrace()
            }
            return result
        }
    }

}