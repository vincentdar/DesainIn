package com.example.cobachatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedemo.FeedAdapter
import com.google.firebase.firestore.FirebaseFirestore

class Feed : AppCompatActivity() {

    //rv data
    lateinit var rvNotes: RecyclerView
    lateinit var soon_to_be_passed_user: User
    val user_id = mutableListOf<String>()
    val username = mutableListOf<String>()
    val pic = mutableListOf<String>()
    val caption = mutableListOf<String>()
    val date = mutableListOf<String>()
    val dc_feed = ArrayList<dcFeed>()

    private lateinit var _progressBarCircular: ProgressBar

    //database
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        rvNotes = findViewById(R.id.rv_feed)
        _progressBarCircular = findViewById(R.id.progress_bar_feed)
        ReadDataFeed()

    }

    fun FeedToDataClass() {
        for (position in user_id.indices) {
            val data = dcFeed(
                user_id[position],
                username[position],
                pic[position],
                caption[position],
                date[position]
            )
            dc_feed.add(data)
        }
    }

    fun PassUser(pass_user: String) {
        db.collection("tbUsers")
            .whereEqualTo("userId", pass_user)
            .get()
            .addOnSuccessListener { result ->

                for (doc in result) {
                    val data = User(
                        doc.data.get("userName").toString(),
                        doc.data.get("profileImage").toString(),
                        doc.data.get("userId").toString(),
                        doc.data.get("desainer").toString()
                    )
                    soon_to_be_passed_user = data
                    Log.d("feed_intent", data.userName.toString())

                    //intent
                    val intent = Intent(this@Feed, DesainerProfActivity::class.java)
                    intent.putExtra("desainer", soon_to_be_passed_user)
                    startActivity(intent)
                }
            }
    }

    private fun ReadDataFeed() {
        db.collection("tbPosts").get()
            .addOnSuccessListener { result_post ->
                db.collection("tbUsers").get()
                    .addOnSuccessListener { result_user ->
                        if(result_user.size() == 0){
                            _progressBarCircular.isGone = true
                        }
                        Log.d("feed_firebase", "listening")
                        user_id.clear()
                        caption.clear()
                        pic.clear()
                        date.clear()
                        dc_feed.clear()
                        for (doc_post in result_post) {
                            Log.d("feed_firebase", doc_post.id)
                            val doc_user_id = doc_post.data.get("userId").toString()
                            val doc_caption = doc_post.data.get("caption").toString()
                            val doc_pic = doc_post.id
                            val doc_date = doc_post.data.get("tanggal").toString()
                            for (doc_user in result_user) {
                                if(doc_user_id == doc_user.data.get("userId").toString()) {
                                    val doc_username = doc_user.data.get("userName").toString()
                                    username.add(doc_username)
                                }
                            }
                            pic.add(doc_pic)
                            user_id.add(doc_user_id)
                            caption.add(doc_caption)
                            date.add(doc_date)
                            Log.d("feed_firebase", "listening")
                        }
                        _progressBarCircular.isGone = true
                        FeedToDataClass()
                        Log.d("feed firebase", "read data - size : " + result_post.size())
                        ShowData()
                    }
                    .addOnFailureListener{
                        Log.d("feed_firebase", it.toString())
                    }
            }

            .addOnFailureListener {
                Log.d("feed firebase", it.message.toString())
            }
    }

    fun ShowData() {
        rvNotes.layoutManager = LinearLayoutManager(this)
        val notesAdapter = FeedAdapter(dc_feed)
        rvNotes.adapter = notesAdapter


        notesAdapter.setOnItemClickCallback(object : FeedAdapter.OnItemClickCallback {
            override fun OnImageClicked(data: dcFeed) {
                Log.d("feed_click", "OnImageClicked is clicked")
                PassUser(data.feed_user_id.toString())
            }

            override fun OnUserNameClicked(data: dcFeed) {
                Log.d("feed_click", "OnUserNameClicked is clicked")
                PassUser(data.feed_user_id.toString())
            }
        })
    }

}