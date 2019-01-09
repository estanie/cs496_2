package com.example.q.cs496_2.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.q.cs496_2.R
import com.example.q.cs496_2.adapters.MusicListAdapter
import com.example.q.cs496_2.managers.MusicId
import com.example.q.cs496_2.models.Music
import com.example.q.cs496_2.models.MusicInfo
import com.example.q.cs496_2.music.SongMetadataReader
import com.facebook.AccessToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_my_music.view.*
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files

class MyMusicFragment : Fragment() {
    private var adapter: MusicListAdapter? = null
    private val REQUEST_CODE_EDIT = 1
    private var accessToken = AccessToken.getCurrentAccessToken()
    private var isLoggedIn = accessToken != null && !accessToken.isExpired()
    var musicList = ArrayList<Music>()

    var mFilename = ""
    lateinit var metadataReader: SongMetadataReader
    var IS_LOGIN = 1004

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
        } else {
            getDataTask(this).execute()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_my_music, container, false)
        // 서버에서 가져오기
        adapter = MusicListAdapter(context!!, musicList)
        view.musicListRecyclerView.adapter = adapter
        view.musicListRecyclerView.layoutManager = LinearLayoutManager(context)
        view.newMusicButton.setOnClickListener {
            val fragment = MusicSelectFragment()
            fragment.setTargetFragment(this, REQUEST_CODE_EDIT)
            fragmentManager!!.beginTransaction().run {
                add(R.id.musicFragment, fragment)
                addToBackStack(null)
                commit()
            }
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataIntent: Intent?) {
        view!!.newMusicButton.show()
        if (requestCode != REQUEST_CODE_EDIT) {
            return
        }
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        Log.e("FILENAME", dataIntent?.extras?.getString("filename"))
        mFilename = dataIntent?.extras?.getString("filename")?.replaceFirst("file://", "")!!.replace("%20", " ")
        MusicAsyncTask(this, mFilename)
            .execute(mFilename.replaceFirst("file://".toRegex(), "").replace("%20".toRegex(), " "))
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
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

    class MusicAsyncTask() : AsyncTask<String, String, String>() {
        var url = ""
        var mFragment: MyMusicFragment? = null
        private val CONNECTION_TIMEOUT = 60000
        private var accessToken = AccessToken.getCurrentAccessToken()
        private var isLoggedIn = accessToken != null && !accessToken.isExpired()
        private var encoded: String? = null
        private var mFilename = ""

        constructor(fragment: MyMusicFragment, filename: String) : this() {
            mFragment = fragment
            mFilename = filename
        }

        override fun doInBackground(vararg values: String?): String {
            uploadMusic(values[0].toString())
            return " "
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            Toast.makeText(mFragment!!.context, R.string.default_upload_success_message, Toast.LENGTH_LONG).show()
        }

        fun uploadMusic(path: String) {
            url = "http://socrip4.kaist.ac.kr:3380/api/music/upload"
            var accessToken = AccessToken.getCurrentAccessToken()
            isLoggedIn = accessToken != null && !accessToken.isExpired()
            if (!isLoggedIn) {
                // facebookLoginAsync.
            }
            var f = File(path)
            val mRequestQueue = Volley.newRequestQueue(mFragment!!.context)
            val mStringRequest = object : StringRequest(Request.Method.POST, url, { s -> }, { e -> }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>(1)
                    encoded = Base64.encodeToString(Files.readAllBytes(f.toPath()), Base64.DEFAULT)
                    params["music"] = encoded!!
                    encoded = null
                    System.gc()
                    var gson = Gson()
                    var jsonString = gson.toJson(MusicInfo(f.name, accessToken.userId.toString()))
                    params["musicInfo"] = jsonString
                    Log.e("MUSIC_INFO", jsonString)

                    return params
                }

                override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                    var str = String(response!!.data)
                    var json = JSONObject(str)
                    var id = json.getString("id")
                    Log.e("RESPONSE", id)
                    MusicId.id = id

                    var metadataReader = SongMetadataReader(mFragment!!.activity as Activity, mFilename)
                    Log.e("PARSE_NETWORK_RESPONSE", "여기이이이이이")
                    Thread(Runnable {
                        mFragment!!.activity!!.runOnUiThread(java.lang.Runnable {
                            mFragment!!.musicList.add(
                                Music(
                                    MusicId.id!!,
                                    metadataReader.mTitle,
                                    metadataReader.mArtist,
                                    false,
                                    mFilename
                                )
                            )
                            mFragment!!.adapter!!.notifyDataSetChanged()
                        })
                    }).start()

                    return super.parseNetworkResponse(response)
                }
            }

            mStringRequest.retryPolicy = object : RetryPolicy {
                override fun getCurrentTimeout() = 15000
                override fun getCurrentRetryCount() = 0
                override fun retry(error: VolleyError) = Unit
            }
            mRequestQueue.add(mStringRequest)
        }
    }

    fun updateAdapter(list: ArrayList<Music>) {
        musicList = list
        adapter = MusicListAdapter(context!!, musicList)
        view!!.musicListRecyclerView.adapter = adapter
        view!!.musicListRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter!!.notifyDataSetChanged()
    }

    class getDataTask() : AsyncTask<String, String, String>() {

        private var accessToken = AccessToken.getCurrentAccessToken()
        private var isLoggedIn = accessToken != null && !accessToken.isExpired()
        private val CONNECTION_TIMEOUT = 60000
        private var mMusicList = ArrayList<Music>()
        var url = "http://socrip4.kaist.ac.kr:3380/api/music/user/"
        lateinit var mFragment: MyMusicFragment

        constructor(fragment: MyMusicFragment) : this() {
            mFragment = fragment

        }

        override fun doInBackground(vararg values: String?): String {
            var accessToken = AccessToken.getCurrentAccessToken()
            isLoggedIn = accessToken != null && !accessToken.isExpired()
            if (!isLoggedIn) {
                // Toast.makeText(mFragment.context, "로그인을 먼저 해주세요!", Toast.LENGTH_LONG).show()
                return " "
            }
            var urlConnection: HttpURLConnection? = null
            try {
                Log.e("URL", url + accessToken.userId.toString())
                var link = URL(url + accessToken.userId.toString())
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
                mMusicList = gson.fromJson(values[0], object : TypeToken<List<Music>>() {}.type)
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
                        result += line
                    }
                } while (line != null)
                inputStream.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return result
        }
    }
}