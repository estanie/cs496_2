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
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.File

class UploadAsyncTask(): AsyncTask<String,String, String>() {
    val url = "http://socrip4.kaist.ac.kr:3380/api/images"
    var mContext: Context? = null
    private val CONNECTION_TIMEOUT = 60000
    private var accessToken = AccessToken.getCurrentAccessToken()
    private var isLoggedIn = accessToken != null && !accessToken.isExpired()
    private var isMulti : Boolean = false

    constructor(context: Context, isMulti: Boolean):this() {
        mContext = context
        this.isMulti = isMulti
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

    // TODO(@estanie): T^T 많이 안올라감...
    fun uploadImage(pos: Int) {
        if (!isLoggedIn) {
            Toast.makeText(mContext, "Log in first", Toast.LENGTH_LONG).show()
        } else {
            val f = File(ImageManager.getImage(pos).path)
            val mRequestQueue = Volley.newRequestQueue(mContext)
            val mStringRequest = object : StringRequest(Request.Method.POST, url, { s -> }, { e ->
                // Toast.makeText(context, e.networkResponse.statusCode, Toast.LENGTH_LONG).show()
            }) {
                val bitmap = BitmapFactory.decodeFile(f.absolutePath)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>(1)
                    params["image"] = getStringImage(bitmap)
                    var gson = Gson()
                    var jsonString = gson.toJson(ImageInfo(f.name, accessToken.userId.toString(), f.absolutePath))
                    params["imageInfo"] = jsonString
                    Log.e("IMAGE_INFO", jsonString)
                    System.gc()
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
    }

    fun getStringImage(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val imageBytes = stream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }
}