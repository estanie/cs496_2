package com.example.q.cs496_2.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.q.cs496_2.R
import com.example.q.cs496_2.models.Address
import kotlinx.android.synthetic.main.address_entry.view.*

class AddListAdapter (val context: Context, val addList: ArrayList<Address>) :
    RecyclerView.Adapter<AddListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder{
        val view = LayoutInflater.from(context).inflate(R.layout.address_entry, parent, false)
        return Holder(view)
    }
    override fun getItemCount(): Int {
        return addList.size
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(addList[position], context)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        val addEntry = itemView?.findViewById<ConstraintLayout>(R.id.addEntry)
        val addPhoto = itemView?.findViewById<ImageView>(R.id.addPhoto)
        val addName = itemView?.findViewById<TextView>(R.id.addName)
        val addNumber = itemView?.findViewById<TextView>(R.id.addNumber)
        fun bind (add: Address, context: Context) {
            if (add.photo != null) {
                addPhoto?.setImageBitmap(add.photo)
            }else{
                addPhoto?.setImageResource(R.mipmap.ic_launcher)
            }
            itemView.addNumber.text=add.number
            itemView.addName.text=add.name


            addEntry?.setOnClickListener{
                val contactUri = Uri.parse(add.address)
                val intent = Intent(Intent.ACTION_VIEW,contactUri)
                if (intent.resolveActivity(context.packageManager)!= null) {
                    context.startActivity(intent)
                }
            }
        }
    }
}