package com.example.cobachatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cobachatapp.databinding.ActivityGalleryBinding
import com.google.firebase.database.FirebaseDatabase
import com.example.cobachatapp.databinding.ItemProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.protobuf.Value

class GalleryActivity : AppCompatActivity() {

    var binding: ActivityGalleryBinding? = null
    var database: FirebaseDatabase? = null
    var users: ArrayList<UserForRealtime>? = null
    var usersAdapter: UserAdapter? = null
    var user: UserForRealtime? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        database = FirebaseDatabase.getInstance()
        users = ArrayList<UserForRealtime>()
        usersAdapter = UserAdapter(this@GalleryActivity, users!!)
        val layoutManager = GridLayoutManager(this@GalleryActivity, 2)
        binding!!.mRec.layoutManager = layoutManager
        database!!.reference.child("user")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(UserForRealtime::class.java)

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        binding!!.mRec.adapter = usersAdapter
        database!!.reference.child("user").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                users!!.clear()
                for(snapshot1 in snapshot.children){
                    val user: UserForRealtime?= snapshot1.getValue(UserForRealtime::class.java)
                    if (!user!!.uid.equals(FirebaseAuth.getInstance().uid)) users!!.add(user)
                }
                usersAdapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("presence")
            .child(currentId!!).setValue("Online")
    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("presence")
            .child(currentId!!).setValue("Offline")
    }
}