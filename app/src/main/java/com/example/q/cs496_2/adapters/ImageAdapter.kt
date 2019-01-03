package com.example.q.cs496_2.adapters

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.q.cs496_2.models.MyImage
import com.example.q.cs496_2.R
import com.example.q.cs496_2.activities.ImageDetailActivity
import com.example.q.cs496_2.helpers.DialogHelper
import com.example.q.cs496_2.managers.ImageManager
import kotlinx.android.synthetic.main.entry_image.view.*
import java.io.File

class ImageAdapter(val context: Context) : RecyclerView.Adapter<ImageAdapter.Holder>() {
    private var mCurrentAnimator: Animator? = null
    private var mShortAnimationDuration: Int = 0

    override fun getItemCount(): Int {
        return ImageManager.getSize()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Holder {
        val holder = Holder(LayoutInflater.from(context).inflate(R.layout.entry_image, parent, false))
        return holder
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        changeHeight(holder)
        holder.bind(context, position)
    }


    inner class Holder(view: View): RecyclerView.ViewHolder(view) {
        val view = view
        fun bind(context: Context, position: Int) {
            val image = ImageManager.getImage(position)
            mShortAnimationDuration = context.resources.getInteger(android.R.integer.config_shortAnimTime)
            view.imgthumb.setOnClickListener {
                // zoomImageFromThumb(view, image)
                val intent = Intent(context, ImageDetailActivity::class.java)
                intent.putExtra("position", position)
                context.startActivity(intent)
            }

            view.imgthumb.setOnLongClickListener {
                DialogHelper().makeYesOrNoDialog(context) {
                    val file = File(image.path)
                    file.delete()
                    context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(image.path))))
                    ImageManager.remove(image)
                    notifyDataSetChanged()
                }
                true
            }

            Glide.with(context)
                .load(File(image.path))
                .into(view.imgthumb)
        }
    }

    fun addImageToList(path: String) {
        ImageManager.add(MyImage(path))
        notifyDataSetChanged()
    }

    private fun changeHeight(holder: Holder) {
        val displayMetrics = DisplayMetrics()
        val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        Log.e("WIDTH", ""+displayMetrics.widthPixels)
        holder.view.imgthumb.layoutParams.width = displayMetrics.widthPixels / 3
        holder.view.imgthumb.layoutParams.height = displayMetrics.widthPixels / 3
    }
}