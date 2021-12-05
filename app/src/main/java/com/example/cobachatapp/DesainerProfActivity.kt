package com.example.cobachatapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
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


        // Get current user data, make sure the home activity has filtered the null user data
        current_user = intent.getParcelableExtra("current_user")!!

        val _btnFollow = findViewById<Button>(R.id.btnFollow)
        val _tvFollower = findViewById<TextView>(R.id.tvFollower)


        // Upper UI Handler
        val _ivProfile = findViewById<ImageView>(R.id.ivProfile)
        _ivProfile.setImageResource(R.drawable.yunjin)

        val _tvUsername = findViewById<TextView>(R.id.tvUsername)
        _tvUsername.setText(current_user.userName)

        val _btnBack = findViewById<ImageButton>(R.id.btnBack)

        _btnBack.setOnClickListener {
            val intent = Intent(this@DesainerProfActivity, HomeActivity::class.java)
            intent.putExtra("current_user", current_user)
            startActivity(intent)
        }

        // Lower UI Handler
        tabLayout = findViewById(R.id.tabLayout)
        viewPager2 = findViewById(R.id.viewPager2)

        var tabs = 1;
        var authenticated = false
        auth.currentUser?.uid?.let { Log.d("Authentication", it) }
        if (auth.currentUser?.uid == current_user.userId) {
            _btnFollow.isInvisible = true
            _tvFollower.isInvisible = false
            authenticated = true
            tabs = 2
        }
        else {
            _btnFollow.isInvisible = false
            _tvFollower.isInvisible = true
            authenticated = false
            tabs = 1
        }

        val adapter = TabProfileAdapter( supportFragmentManager, lifecycle, current_user, tabs, authenticated)
        viewPager2.adapter = adapter

        val optionsArray = arrayOf("Post", "Upload")
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = optionsArray[position]
        }.attach()

    }


}