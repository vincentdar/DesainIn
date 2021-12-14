package com.example.firebasedemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cobachatapp.R
import com.example.cobachatapp.dcFeed
import org.w3c.dom.Text

class FeedAdapter (private val listNotes : ArrayList<dcFeed>):
    RecyclerView.Adapter<FeedAdapter.ListViewHolder>(){

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tv_user_id : TextView = itemView.findViewById(R.id.tv_username)
        var iv_feed : ImageView = itemView.findViewById(R.id.iv_feed)
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
        holder.tv_user_id.setText(notes.feed_user_id)
        holder.tv_caption.setText(notes.feed_caption)
        holder.tv_date.setText(notes.feed_date)
    }

    override fun getItemCount(): Int {
        return listNotes.size
    }
}