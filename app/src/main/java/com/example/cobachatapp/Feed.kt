package com.example.cobachatapp

import android.graphics.Insets.add
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedemo.FeedAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Feed : AppCompatActivity() {

    //rv data
    lateinit var rvNotes: RecyclerView
    val user_id = mutableListOf<String>()
    val username = mutableListOf<String>()
    val caption = mutableListOf<String>()
    val date = mutableListOf<String>()
    val d = ArrayList<dcFeed>()

    //database
    private var db:FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        rvNotes = findViewById(R.id.rv_feed)
        ReadDataFeed()

    }

    fun FeedToDataClass(){
        for(position in user_id.indices){
            val data = dcFeed(
                user_id[position],
                caption[position],
                date[position]
            )
            d.add(data)
        }
    }

    private fun ReadDataFeed(){
        db.collection("tbPosts").get()
            .addOnSuccessListener { result ->
                Log.d("feed firebase", "listening")
                user_id.clear()
                caption.clear()
                date.clear()
                d.clear()
                for (doc in result){
                    val doc_user_id = doc.data.get("userId").toString()
                    val doc_caption = doc.data.get("caption").toString()
                    val doc_date = doc.data.get("tanggal").toString()
                    user_id.add(doc_user_id)
                    caption.add(doc_caption)
                    date.add(doc_date)
                }
                FeedToDataClass()
                Log.d("feed firebase", "read data - size : " + result.size())
                ShowData()
            }
            .addOnFailureListener{
                Log.d("feed firebase", it.message.toString())
            }
    }

    fun ShowData(){
        rvNotes.layoutManager = LinearLayoutManager(this)
        val notesAdapter = FeedAdapter(d)
        rvNotes.adapter = notesAdapter
    }
}