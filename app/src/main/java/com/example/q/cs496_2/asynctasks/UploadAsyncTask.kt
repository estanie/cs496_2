package com.example.q.cs496_2.asynctasks

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.q.cs496_2.managers.ImageManager
import com.example.q.cs496_2.models.ImageInfo
import com.facebook.AccessToken
import com.facebook.login.widget.LoginButton
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileReader

class UploadAsyncTask(): AsyncTask<String,String, String>() {
    var url = ""
    var mContext: Context? = null
    private val CONNECTION_TIMEOUT = 60000
    private var accessToken = AccessToken.getCurrentAccessToken()
    private var isLoggedIn = accessToken != null && !accessToken.isExpired()
    private var isMulti : Boolean = false
    private var button :LoginButton? = null
    private var encoded :String? = null
    constructor(context: Context, button: LoginButton?, isMulti: Boolean):this() {
        mContext = context
        this.isMulti = isMulti
        this.button = button
    }

    override fun doInBackground(vararg values: String?): String {
        if (isMulti) {
            for (i in 0..ImageManager.getSize()) {
                uploadImage(i)
            }
        }
        else uploadImage(values[0]!!.toInt())
        return " "
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        Toast.makeText(mContext, "Uploaded!", Toast.LENGTH_LONG).show()
    }
    fun uploadMusic(path: String) {
        url = "http://socrip4.kaist.ac.kr:3380/api/music/upload"
        var accessToken = AccessToken.getCurrentAccessToken()
        isLoggedIn = accessToken != null && !accessToken.isExpired()
        if (!isLoggedIn) {
            // facebookLoginAsync.
        }
        var f = File(path)
        readFileToString(path)
/*        val mRequestQueue = Volley.newRequestQueue(mContext)
        val mStringRequest = object: StringRequest(Request.Method.POST, url, {s-> },{e ->}) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>(1)
                params["music"] = readFileToString(path)
                encoded = null
                System.gc()
                var gson = Gson()
                var jsonString = gson.toJson(MusicInfo(f.name, accessToken.userId.toString()))
                params["musicInfo"] = jsonString
                Log.e("MUSIC_INFO", jsonString)

                return params
            }
        }

        mStringRequest.retryPolicy = object : RetryPolicy {
            override fun getCurrentTimeout() = 15000
            override fun getCurrentRetryCount() = 0
            override fun retry(error: VolleyError) = Unit
        }
        mRequestQueue.add(mStringRequest)*/
    }

    // TODO(@estanie): T^T 많이 안올라감...
    fun uploadImage(pos: Int) {
        url = "http://socrip4.kaist.ac.kr:3380/api/images"
        var accessToken = AccessToken.getCurrentAccessToken()
        isLoggedIn = accessToken != null && !accessToken.isExpired()
        if (!isLoggedIn) {
            button?.callOnClick()
        }
            val f = File(ImageManager.getImage(pos).path)
            val mRequestQueue = Volley.newRequestQueue(mContext)
            val mStringRequest = object : StringRequest(Request.Method.POST, url, { s -> }, { e ->
                // Toast.makeText(context, e.networkResponse.statusCode, Toast.LENGTH_LONG).show()
            }) {
                val bitmap = BitmapFactory.decodeFile(f.absolutePath)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>(1)
                    params["image"] = getStringImage(bitmap)
                    encoded = null
                    System.gc()
                    var gson = Gson()
                    var jsonString = gson.toJson(ImageInfo(f.name, accessToken.userId.toString(), f.absolutePath))
                    params["imageInfo"] = jsonString
                    Log.e("IMAGE_INFO", jsonString)

                    return params
                }
            }
            mStringRequest.retryPolicy = object : RetryPolicy {
                override fun getCurrentTimeout() = 15000
                override fun getCurrentRetryCount() = 0
                override fun retry(error: VolleyError) = Unit
            }
            mRequestQueue.add(mStringRequest)
    }

    fun getStringImage(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val imageBytes = stream.toByteArray()
        stream.close()
        encoded = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return encoded!!
    }

    @Throws(java.io.IOException::class)
    private fun readFileToString(filePath: String): String {

        val fileData = StringBuffer()
        val reader = BufferedReader(
            FileReader(filePath)
        )
        var buf = CharArray(1024)

        var numRead = 0
        while (true) {
            numRead = reader.read(buf)
            if (numRead == -1) break
            else {
                val readData = String(buf, 0, numRead)
                fileData.append(readData)
                buf = CharArray(1024)
            }
        }

        reader.close()
        Log.e("FILESTRING", fileData.toString())
        return fileData.toString()
    }
}