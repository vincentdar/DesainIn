package com.example.cobachatapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.NullPointerException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class DesainerProfActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var current_user: User

    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_desainer_profile_page)
        auth = FirebaseAuth.getInstance()
        getCurrentUserData()

        // Upper UI Handler
        val _ivProfile = findViewById<ImageView>(R.id.ivProfile)
        _ivProfile.setImageResource(R.drawable.yunjin)

        val _tvUsername = findViewById<TextView>(R.id.tvUsername)
        _tvUsername.setText(current_user.userName)

        val _btnBack = findViewById<ImageButton>(R.id.btnBack)

        _btnBack.setOnClickListener {
            val intent = Intent(this@DesainerProfActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        // Lower UI Handler
        tabLayout = findViewById(R.id.tabLayout)
        viewPager2 = findViewById(R.id.viewPager2)

        val adapter = TabProfileAdapter( supportFragmentManager, lifecycle)
        viewPager2.adapter = adapter

        val optionsArray = arrayOf("Post", "Upload")
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = optionsArray[position]
        }.attach()

    }

    fun getCurrentUserData() {
        val user = auth.currentUser
        if (user != null) {
            val nama = user.displayName
            val photo = user.photoUrl
            val uid = user.uid
//            var desainer = "2"

//            databaseReference  = databaseReference.child(uid)
//            databaseReference.get().addOnSuccessListener {
//                desainer = it.child("desainer").getValue().toString()
//                Log.d("Firebase Database", "Desainer: " + desainer)
//            }

//            val userListener = object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    val desainer_value = dataSnapshot.child("desainer").getValue().toString()
//                    Log.d("Firebase Database", "Desainer: " + desainer_value)
//                    if (desainer_value == "1") {
//                        desainer = "1"
//                    } else {
//                        desainer = "0"
//                    }
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    // Getting Post failed, log a message
//                    Log.d("Firebase Database", "Cancelled Reading Data")
//                }
//            }
//            databaseReference.addListenerForSingleValueEvent(userListener)
            current_user = User(nama, photo, uid, "0")
        }
        else {
            current_user = User("Guest",  Uri.EMPTY, "", "0")
        }
    }
}