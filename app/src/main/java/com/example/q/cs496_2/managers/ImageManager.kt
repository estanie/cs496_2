package com.example.q.cs496_2.managers

import android.content.Context
import android.provider.MediaStore
import com.example.q.cs496_2.models.MyImage

object ImageManager {
    private var imageList = ArrayList<MyImage>()

    fun getSize() : Int {
        return imageList.size
    }
    fun getImage(position: Int): MyImage {
        return imageList[position]
    }
    fun remove(image: MyImage) {
        imageList.remove(image)
    }
    fun add(image: MyImage) {
        imageList.add(0, image)
    }
    fun getAllShownImagesPath(context: Context) : java.util.ArrayList<MyImage> {
        val uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection: Array<String> = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        var cursor = context.contentResolver.query(uri,projection, null, null, null)
        val columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        while (cursor.moveToNext()) {
            imageList.add(0, MyImage(cursor.getString(columnIndexData)))
        }
        return imageList
    }
}