package com.example.q.cs496_2.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.q.cs496_2.R
import com.example.q.cs496_2.adapters.ImageAdapter
import com.example.q.cs496_2.asynctasks.UploadAsyncTask
import com.example.q.cs496_2.managers.ImageManager
import com.example.q.cs496_2.models.MyImage
import com.facebook.login.widget.LoginButton
import kotlinx.android.synthetic.main.fragment_gallery.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class GalleryFragment: Fragment() {
    private var adapter: ImageAdapter? = null

    val REQUEST_IMAGE_CAPTURE = 1
    var mCurrentPhotoPath = ""
    val IMAGE_PATH = "/storage/emulated/0/DCIM/cs496_2"
    var imageList : ArrayList<MyImage>? = null

    override fun onAttach(cotext: Context) {
        super.onAttach(context!!)
        imageList = ImageManager.getAllShownImagesPath(context!!)
        adapter = ImageAdapter(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        setHasOptionsMenu(true)
        view.imageGrid.adapter = adapter
        view.imageGrid.layoutManager = GridLayoutManager(context, 3)

        view.addImgFab.setOnClickListener { view ->
            dispatchTakePictureIntent()
        }
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_backup -> {
                uploadAllImage()
                return true
            }
            R.id.action_cloud_sync -> {
                downloadExceptExistImage()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun downloadExceptExistImage() {

    }
    // TODO(@estanie):
    private fun uploadAllImage() {
        // UploadAsyncTask(context!!, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0.toString())
    }
    // TODO(@estanie): It would be seperate to camera utils, Also do not use this way T-T. :0
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(context!!, context!!.packageName+".fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    galleryAddPic()
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = File(IMAGE_PATH)
        if (!storageDir.exists()) storageDir.mkdir()
        return File.createTempFile("JPEG_${timeStamp}_",".jpg",storageDir).apply {
            mCurrentPhotoPath = absolutePath
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also {mediaScanIntent ->
            val f = File(mCurrentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            Log.e("mCurrentPhotoPath", mediaScanIntent.data.path+"")
            context!!.sendBroadcast(mediaScanIntent)
            adapter!!.addImageToList(mCurrentPhotoPath)
            // TODO(@estanie): If take picture canceled, remove this.
        }
    }
}