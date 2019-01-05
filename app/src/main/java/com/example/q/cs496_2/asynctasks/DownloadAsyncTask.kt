package com.example.q.cs496_2.asynctasks

import android.os.AsyncTask
import java.net.HttpURLConnection
import java.net.URL

class DownloadAsyncTask: AsyncTask<String, String, String>() {
    private val url = "http://socrip4.kaist.ac.kr:3380/api/images/download/"
    override fun doInBackground(vararg params: String?): String {
        var urlConnection: HttpURLConnection? = null

        return " "
    }
}