package com.example.cobachatapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.lang.NullPointerException
import com.google.android.gms.tasks.Task as Task1

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var current_user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_home)

        val _tvUsername = findViewById<TextView>(R.id.tvUsername)
        val _tvUsertoken = findViewById<TextView>(R.id.tvUsertoken)
        val _tvIsDesainer = findViewById<TextView>(R.id.tvIsDesainer)
        val _btnProfDesainer = findViewById<Button>(R.id.btnProfDesainer)

        auth = FirebaseAuth.getInstance()
        databaseReference  = FirebaseDatabase.getInstance().getReference("Users")
        getCurrentUserData()

        _tvUsername.setText(current_user.userName)
        _tvUsertoken.setText(current_user.userId)
        _tvIsDesainer.setText(current_user.desainer)

        _btnProfDesainer.setOnClickListener {
            val intent = Intent(this@HomeActivity, DesainerProfActivity::class.java)
            startActivity(intent)
        }
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