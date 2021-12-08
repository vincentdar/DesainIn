package com.example.cobachatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import java.lang.NullPointerException

class ClientProfActivity : AppCompatActivity() {
    lateinit var current_user: User
    //vd kentang
    //kentang
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_client_prof)

        // Get current user data, make sure the home activity has filtered the null user data
        current_user = intent.getParcelableExtra("current_user")!!

        // Upper UI handler
        val _ivProfile = findViewById<ImageView>(R.id.ivProfile)
        val _tvUsername = findViewById<TextView>(R.id.tvUsername)
        _tvUsername.setText(current_user.userName)

        val _btnBack = findViewById<ImageButton>(R.id.btnBack)
        _btnBack.setOnClickListener {
            val intent = Intent(this@ClientProfActivity, HomeActivity::class.java)
            intent.putExtra("current_user", current_user)
            startActivity(intent)
        }
    }
}