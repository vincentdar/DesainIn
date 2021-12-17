package com.example.firebasedemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cobachatapp.R
import com.example.cobachatapp.dcCommission
import org.w3c.dom.Text

class CommissionAdapter (private val listNotes : ArrayList<dcCommission>):
    RecyclerView.Adapter<CommissionAdapter.ListViewHolder>(){

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tv_title : TextView = itemView.findViewById(R.id.tv_designer_name)
        var tv_client_name : TextView = itemView.findViewById(R.id.tv_client)
        var tv_designer_name : TextView = itemView.findViewById(R.id.tv_designer_name)
        var tv_desc : TextView = itemView.findViewById(R.id.tv_desc)
        var tv_date : TextView = itemView.findViewById(R.id.tv_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var view : View =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_feed,parent,false)
        return  ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var notes = listNotes[position]
        holder.tv_title.setText(notes.comm_title)
        holder.tv_client_name.setText(notes.comm_client_name)
        holder.tv_designer_name.setText(notes.comm_designer_name)
        holder.tv_desc.setText(notes.comm_desc)
        holder.tv_date.setText(notes.comm_date)
    }

    override fun getItemCount(): Int {
        return listNotes.size
    }
}