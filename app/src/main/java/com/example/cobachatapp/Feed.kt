package com.example.cobachatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedemo.FeedAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Feed : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    //rv data
    lateinit var rvNotes: RecyclerView
    val user_id = mutableListOf<String>()
    val username = mutableListOf<String>()
    val caption = mutableListOf<String>()
    val date = mutableListOf<String>()
    val dc_feed = ArrayList<dcFeed>()

    //database
    private var db:FirebaseFirestore = FirebaseFirestore.getInstance()
    private var current_user = User("Guest",  "", "", "0")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        auth = FirebaseAuth.getInstance()
        rvNotes = findViewById(R.id.rv_feed)
        ReadDataFeed()

        val _ibProfile = findViewById<ImageButton>(R.id.ibProfile)
        val _ibChat = findViewById<ImageButton>(R.id.ibChat)
        val _ibCommission = findViewById<ImageButton>(R.id.ibCommission)
        val _ibExit = findViewById<ImageButton>(R.id.ibExit)

        current_user = StaticHolder.get_current_user()

        _ibProfile.setOnClickListener {
            if (current_user.desainer == "1") {
                val intent = Intent(this@Feed, DesainerProfActivity::class.java)
                intent.putExtra("current_user", current_user)
                intent.putExtra("desainer", current_user)
                startActivity(intent)
            }
            else {
                val intent = Intent(this@Feed, ClientProfActivity::class.java)
                intent.putExtra("current_user", current_user)
                startActivity(intent)
            }
        }

        _ibChat.setOnClickListener{
            val intent = Intent(this@Feed, GalleryActivity::class.java)
            startActivity(intent)
        }


        _ibExit.setOnClickListener {
            auth.signOut()
            StaticHolder.set_guest()
            val intent = Intent(this@Feed, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        _ibCommission.setOnClickListener{
            val intent = Intent(this@Feed, Commission::class.java)
            startActivity(intent)
        }

    }

    fun FeedToDataClass(){
        for(position in user_id.indices){
            val data = dcFeed(
                user_id[position],
                caption[position],
                date[position]
            )
            dc_feed.add(data)
        }
    }

    private fun ReadDataFeed(){
        db.collection("tbPosts").get()
            .addOnSuccessListener { result ->
                Log.d("feed firebase", "listening")
                user_id.clear()
                caption.clear()
                date.clear()
                dc_feed.clear()
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
        val notesAdapter = FeedAdapter(dc_feed)
        rvNotes.adapter = notesAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}