package com.example.firebasedemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.cobachatapp.R
import com.example.cobachatapp.dcCommission

class CommissionAdapter (private val listNotes : ArrayList<dcCommission>):
    RecyclerView.Adapter<CommissionAdapter.ListViewHolder>(){

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun OnBodyClicked(data:dcCommission)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tv_title : TextView = itemView.findViewById(R.id.tv_commission)
        var tv_client_name : TextView = itemView.findViewById(R.id.tv_client_name)
        var tv_designer_name : TextView = itemView.findViewById(R.id.tv_designer_name)
        var tv_desc : TextView = itemView.findViewById(R.id.tv_desc)
        var tv_date : TextView = itemView.findViewById(R.id.tv_date)
        var rv_parent : ConstraintLayout = itemView.findViewById(R.id.container_comm_detail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var view : View =
            LayoutInflater.from(parent.context).inflate(R.layout.recylerview_commission,parent,false)
        return  ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var notes = listNotes[position]
        holder.tv_title.setText(notes.comm_title)
        holder.tv_client_name.setText(notes.comm_client_name)
        holder.tv_designer_name.setText(notes.comm_designer_name)
        holder.tv_desc.setText(notes.comm_desc)
        holder.tv_date.setText(notes.comm_date)

        holder.rv_parent.setOnClickListener {
            onItemClickCallback.OnBodyClicked(listNotes[position])
        }
    }

    override fun getItemCount(): Int {
        return listNotes.size
    }

}