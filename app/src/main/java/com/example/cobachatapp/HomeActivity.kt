package com.example.cobachatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Gallery
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.NullPointerException

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

        current_user = StaticHolder.get_current_user()

        val _tvUsername = findViewById<TextView>(R.id.tvUsername)
        val _tvUsertoken = findViewById<TextView>(R.id.tvUsertoken)
        val _tvIsDesainer = findViewById<TextView>(R.id.tvIsDesainer)
        val _btnProf = findViewById<Button>(R.id.btnProf)
        val _btnDesainer = findViewById<Button>(R.id.btnDesainer)
        val _btnLogout = findViewById<Button>(R.id.btnLogout)
        val _btnFeed = findViewById<Button>(R.id.btnFeed)
        val _btnComm = findViewById<Button>(R.id.btnCommission)
        val _btnSearch = findViewById<Button>(R.id.btnGallery)

        auth = FirebaseAuth.getInstance()

        _tvUsername.setText(current_user.userName)
        _tvUsertoken.setText(current_user.userId)
        _tvIsDesainer.setText(current_user.desainer)

        _btnProf.setOnClickListener {
            if (current_user.desainer == "1") {
                val intent = Intent(this@HomeActivity, DesainerProfActivity::class.java)
                intent.putExtra("current_user", current_user)
                intent.putExtra("desainer", current_user)
                startActivity(intent)
            }
            else {
                val intent = Intent(this@HomeActivity, ClientProfActivity::class.java)
                intent.putExtra("current_user", current_user)
                startActivity(intent)
            }
        }

        _btnSearch.setOnClickListener{
            val intent = Intent(this@HomeActivity, GalleryActivity::class.java)
            startActivity(intent)
        }

        _btnDesainer.setOnClickListener {
            var user = User("Vincent", "YqFsvj0IRUQYCw3fW8stCNkhOH72",
                "YqFsvj0IRUQYCw3fW8stCNkhOH72", "1")
            val intent = Intent(this@HomeActivity, DesainerProfActivity::class.java)
            intent.putExtra("current_user", current_user)
            intent.putExtra("desainer", user)
            startActivity(intent)
        }

        _btnLogout.setOnClickListener {
            auth.signOut()
            StaticHolder.set_guest()
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        _btnFeed.setOnClickListener{
            val intent = Intent(this@HomeActivity, Feed::class.java)
            startActivity(intent)
        }

        _btnComm.setOnClickListener{
            val intent = Intent(this@HomeActivity, Commission::class.java)
            startActivity(intent)
        }

    }


}