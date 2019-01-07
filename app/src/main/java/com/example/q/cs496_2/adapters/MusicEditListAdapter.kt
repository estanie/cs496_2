package com.example.q.cs496_2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.q.cs496_2.R
import com.example.q.cs496_2.models.Music
import com.example.q.cs496_2.views.WaveformView
import kotlinx.android.synthetic.main.entry_edit_music.view.*

class MusicEditListAdapter(val context: Context, val editorList: ArrayList<Music>) :
    RecyclerView.Adapter<MusicEditListAdapter.Holder>() {
    private var mEditorList = editorList
    fun add(music: Music) {
        mEditorList.add(music)
    }

    override fun getItemCount(): Int {
        return mEditorList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Holder {
        val holder = Holder(LayoutInflater.from(context).inflate(R.layout.entry_edit_music, parent, false))
        return holder
    }

    override fun onBindViewHolder(holder: MusicEditListAdapter.Holder, position: Int) {
        holder.bind(mEditorList[position], context)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!), WaveformView.WaveformListener {
        private var mWidth = 0

        fun bind(music: Music, context: Context) {
            // wave 그거 보여줘야함.
        }

        /**
         * Every time we get a message that our waveform drew, see if we need to
         * animate and trigger another redraw.
         */
        override fun waveformDraw() {
            mWidth = itemView.editMusicWaveform.getMeasuredWidth()
            updateDisplay()
        }

        override fun waveformTouchStart(x: Float) {}

        override fun waveformTouchMove(x: Float) {}

        override fun waveformTouchEnd() {}

        override fun waveformFling(vx: Float) {}

        override fun waveformZoomIn() {}

        override fun waveformZoomOut() {}

        @Synchronized
        private fun updateDisplay() {
            itemView.editMusicWaveform.invalidate()
        }
    }
}