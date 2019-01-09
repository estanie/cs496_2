package com.example.q.cs496_2.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.q.cs496_2.R
import com.example.q.cs496_2.adapters.MusicListAdapter
import com.example.q.cs496_2.models.Music
import com.facebook.AccessToken
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_popular_music.view.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class PopularMusicFragment : androidx.fragment.app.Fragment() {
    var IS_LOGIN = 1004
    private var adapter: MusicListAdapter? = null
    private var musicList = ArrayList<Music>()
    private var accessToken = AccessToken.getCurrentAccessToken()
    private var isLoggedIn = accessToken != null && !accessToken.isExpired()
    override fun onStart() {
        super.onStart()
        var accessToken = AccessToken.getCurrentAccessToken()
        isLoggedIn = accessToken != null && !accessToken.isExpired()
        if (!isLoggedIn) {
            val fragment = SettingFragment()
            fragment.setTargetFragment(this, IS_LOGIN)
            fragmentManager!!.beginTransaction().run {
                add(R.id.mainLayout, fragment)
                addToBackStack(null)
                commit()
            }
        }
        else {
            getDataTask(this).execute()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_popular_music, container, false)

        return view
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            Log.e("POPULAR", "VISIBLE~~~")
            var accessToken = AccessToken.getCurrentAccessToken()
            isLoggedIn = accessToken != null && !accessToken.isExpired()
            if (!isLoggedIn) {
                val fragment = SettingFragment()
                fragment.setTargetFragment(this, IS_LOGIN)
                fragmentManager!!.beginTransaction().run {
                    add(R.id.mainLayout, fragment)
                    addToBackStack(null)
                    commit()
                }
            } else {
                getDataTask(this).execute()
            }
        }
    }

    fun updateAdapter(list: ArrayList<Music>) {
        musicList = list
        adapter = MusicListAdapter(context!!, musicList)
        view!!.popularList.adapter = adapter
        view!!.popularList.layoutManager = LinearLayoutManager(context)
        adapter!!.notifyDataSetChanged()
    }
    class getDataTask(): AsyncTask<String, String, String>() {
        private val CONNECTION_TIMEOUT = 60000
        private var mMusicList = ArrayList<Music>()
        var url = "http://socrip4.kaist.ac.kr:3380/api/music/popular"
        lateinit var mFragment : PopularMusicFragment
        constructor(fragment: PopularMusicFragment):this() {
            mFragment = fragment

        }

        override fun doInBackground(vararg values: String?): String {
            var urlConnection: HttpURLConnection? = null
            try {
                var link = URL(url)
                urlConnection = link.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = CONNECTION_TIMEOUT
                var inString = streamToString(urlConnection.inputStream)
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
                Thread(Runnable {
                    mFragment!!.activity!!.runOnUiThread(java.lang.Runnable {
                        mFragment!!.musicList = mMusicList
                        mFragment!!.adapter = MusicListAdapter(mFragment.context!!, mFragment.musicList)
                        mFragment.view!!.popularList.adapter = mFragment.adapter
                        mFragment.view!!.popularList.layoutManager = LinearLayoutManager(mFragment.context)
                        mFragment!!.adapter!!.notifyDataSetChanged()
                    })
                }).start()
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