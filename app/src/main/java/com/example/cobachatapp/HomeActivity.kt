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
    private var current_user = User("Guest",  "", "", "0")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_home)

        val parcel_user = intent.getParcelableExtra<User>("current_user")
        if (parcel_user != null) {
            current_user = parcel_user
        }

        val _tvUsername = findViewById<TextView>(R.id.tvUsername)
        val _tvUsertoken = findViewById<TextView>(R.id.tvUsertoken)
        val _tvIsDesainer = findViewById<TextView>(R.id.tvIsDesainer)
        val _btnProfDesainer = findViewById<Button>(R.id.btnProfDesainer)

        auth = FirebaseAuth.getInstance()

        _tvUsername.setText(current_user.userName)
        _tvUsertoken.setText(current_user.userId)
        _tvIsDesainer.setText(current_user.desainer)

        _btnProfDesainer.setOnClickListener {
            if (current_user.desainer == "1") {
                val intent = Intent(this@HomeActivity, DesainerProfActivity::class.java)
                intent.putExtra("current_user", current_user)
                startActivity(intent)
            }
            else {
                val intent = Intent(this@HomeActivity, ClientProfActivity::class.java)
                intent.putExtra("current_user", current_user)
                startActivity(intent)
            }

        }

        current_user.desainer?.let { Log.d("Current User", it) }

    }


}