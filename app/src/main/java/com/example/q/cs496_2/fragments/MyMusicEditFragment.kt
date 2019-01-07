package com.example.q.cs496_2.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.q.cs496_2.R
import com.example.q.cs496_2.adapters.MusicEditListAdapter
import com.example.q.cs496_2.adapters.MusicListAdapter
import com.example.q.cs496_2.models.Music
import com.example.q.cs496_2.music.SongMetadataReader
import kotlinx.android.synthetic.main.fragment_edit_music.view.*

class MyMusicEditFragment : Fragment() {
    private var adapter: MusicEditListAdapter? = null
    private val REQUEST_CODE_EDIT = 1
    private var musicList = ArrayList<Music>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataIntent: Intent?) {
        view!!.addNewInstructorFab.show()

        if (requestCode != REQUEST_CODE_EDIT) {
            return
        }

        if (resultCode != RESULT_OK) {
            return
        }

        var mFilename = dataIntent!!.extras.getString("filename").replaceFirst("file://", "").replace("%20", " ")
        var metadataReader = SongMetadataReader(activity as Activity, mFilename)
        // TODO(estanie): 모양은 추가가 되는디요... 왜 안보일까요...?
        adapter!!.add(Music(metadataReader.mTitle, metadataReader.mArtist, mFilename))
        adapter!!.notifyItemInserted(musicList.size - 1)
        // recyclerview에 waveformDraw

        //finish();  // TODO(nfaralli): why would we want to quit the app here?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_edit_music, container, false)
       /* adapter = MusicEditListAdapter(context!!, musicList)
        view.myInstructorView.adapter = adapter
        view.myInstructorView.layoutManager = LinearLayoutManager(context)
       */ view.addNewInstructorFab.setOnClickListener {
            view.addNewInstructorFab.hide()
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
}