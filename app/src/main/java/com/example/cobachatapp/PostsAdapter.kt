package com.example.cobachatapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView

class PostsAdapter (private val listPosts: ArrayList<dcPost>,
                    private val listImages: ArrayList<ByteArray>,
                    private val listUUID: ArrayList<String>,
                    private val authenticated: Boolean) :
    RecyclerView.Adapter<PostsAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback : OnItemClickCallback

    interface OnItemClickCallback {
        fun OnItemDelete(uuid: String)
    }

    fun SetOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _tvUsername : TextView = itemView.findViewById(R.id.tvUsername)
        var _tvTanggal : TextView = itemView.findViewById(R.id.tvTanggal)
        var _ivPost : ImageView = itemView.findViewById(R.id.ivPost)
        var _tvCaption : TextView = itemView.findViewById(R.id.tvCaption)
        var _ibDelete : ImageButton = itemView.findViewById(R.id.ibDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_posts, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var post = listPosts[position]

        // UserID harus diganti ke username terdahulu
        holder._tvUsername.setText(post.userId)
        holder._tvTanggal.setText(post.tanggal)
        holder._tvCaption.setText(post.caption)

        // Handle Images
        if (listImages[position] != byteArrayOf(0x0)) {
            var images = listImages[position]
            var bmp : Bitmap = BitmapFactory.decodeByteArray(images, 0, images.size)
            holder._ivPost.setImageBitmap(bmp)
        }
        else {
            holder._ivPost.setImageResource(0)
        }


        // Handle Button
        holder._ibDelete.setOnClickListener {
            onItemClickCallback.OnItemDelete(listUUID[position])
        }
        // Gone the delete button if not authenticated
        holder._ibDelete.isGone = !authenticated

    }



    override fun getItemCount(): Int {
        return listPosts.size
    }
}