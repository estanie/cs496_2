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
import com.facebook.login.widget.LoginButton
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_my_music.view.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.nio.file.Files

class MyMusicFragment : androidx.fragment.app.Fragment() {
    private var adapter: MusicListAdapter? = null
    private val REQUEST_CODE_EDIT = 1
    private var musicList = ArrayList<Music>()

    fun update(music: Music) {
        activity!!.runOnUiThread({
            object: Runnable {
                override fun run() {
                    Log.e("Update", "update!!!!")
                    adapter!!.add(music)
                    adapter!!.notifyDataSetChanged()
                }
            }
        })
    }

    var mFilename = ""
    lateinit var metadataReader : SongMetadataReader
    override fun onStart() {
        super.onStart()
        Log.e("ONSTART", "on START~~~~~~~~")
        adapter = MusicListAdapter(context!!, musicList)
        view!!.musicListRecyclerView.adapter = adapter
        view!!.musicListRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter!!.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_my_music, container, false)
        // 서버에서만 가져오기

        adapter = MusicListAdapter(context!!, musicList)
        view.musicListRecyclerView.adapter = adapter
        view.musicListRecyclerView.layoutManager = LinearLayoutManager(context)

        view.newMusicButton.setOnClickListener {
            val fragment = MusicSelectFragment()
            view.newMusicButton.hide()
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
        adapter!!.notifyItemInserted(musicList.size - 1)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (view!= null)
                view!!.newMusicButton.show()
        } else {
            if (view!= null)
                view!!.newMusicButton.hide()
        }
    }

    class MusicAsyncTask(): AsyncTask<String, String, String>() {
        var url = ""
        var mFragment: MyMusicFragment? = null
        private val CONNECTION_TIMEOUT = 60000
        private var accessToken = AccessToken.getCurrentAccessToken()
        private var isLoggedIn = accessToken != null && !accessToken.isExpired()
        private var encoded :String? = null
        private var mFilename = ""

        constructor(fragment: MyMusicFragment, filename: String):this() {
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
            if (!isLoggedIn) {
                // facebookLoginAsync.
            }
            var f = File(path)
            val mRequestQueue = Volley.newRequestQueue(mFragment!!.context)
            val mStringRequest = object: StringRequest(Request.Method.POST, url, { s-> },{ e ->}) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>(1)
                    encoded = Base64.encodeToString(Files.readAllBytes(f.toPath()), Base64.DEFAULT)
                    params["music"] = encoded!!
                    Log.e("ENCODED", encoded!!)
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
                    mFragment!!.update(Music(MusicId.id!!, metadataReader.mTitle, metadataReader.mArtist, mFilename))
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
}