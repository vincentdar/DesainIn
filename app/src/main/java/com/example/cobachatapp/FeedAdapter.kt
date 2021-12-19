package com.example.firebasedemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cobachatapp.R
import com.example.cobachatapp.dcFeed
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlin.coroutines.coroutineContext

class FeedAdapter (private val listNotes : ArrayList<dcFeed>):
    RecyclerView.Adapter<FeedAdapter.ListViewHolder>(){

    private lateinit var onItemClickCallback: OnItemClickCallback
    var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    interface OnItemClickCallback {
        fun OnImageClicked(data:dcFeed)
        fun OnUserNameClicked(data:dcFeed)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tv_username : TextView = itemView.findViewById(R.id.tv_client_name)
        var iv_feed_pic : ImageView = itemView.findViewById(R.id.iv_feed_pic)
        var tv_caption : TextView = itemView.findViewById(R.id.tv_caption)
        var tv_date : TextView = itemView.findViewById(R.id.tv_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var view : View =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_feed,parent,false)
        return  ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var notes = listNotes[position]
        holder.tv_username.setText(notes.feed_username)
        holder.tv_caption.setText(notes.feed_caption)
        holder.tv_date.setText(notes.feed_date)

        val storage_ref = FirebaseStorage.getInstance().reference
        val image_ref = storage_ref.child("images/" +  notes.feed_pic)
        val ONE_MEGABYTE: Long = 1024 * 1024
        image_ref.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener {
                Log.d("feed_firebase" , image_ref.toString())
                var bmp : Bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                holder.iv_feed_pic.setImageBitmap(bmp)
            }
            .addOnFailureListener{
                Log.d("feed_firebase", it.toString())
            }

        holder.iv_feed_pic.setOnClickListener{
            onItemClickCallback.OnImageClicked(listNotes[position])
        }
        holder.tv_username.setOnClickListener{
            onItemClickCallback.OnUserNameClicked(listNotes[position])
        }
    }


    override fun getItemCount(): Int {
        return listNotes.size
    }
}