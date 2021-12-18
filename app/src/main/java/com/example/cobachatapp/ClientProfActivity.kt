package com.example.cobachatapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import java.lang.NullPointerException

class ClientProfActivity : AppCompatActivity() {
    lateinit var current_user: User
    lateinit var _ivProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_client_prof)

        // Get current user data, make sure the home activity has filtered the null user data
        current_user = StaticHolder.get_current_user()

        // Upper UI handler
        _ivProfile = findViewById<ImageView>(R.id.ivProfile)
        readProfileImage()

        val _tvUsername = findViewById<TextView>(R.id.tvUsername)

        val _btnChat = findViewById<ImageButton>(R.id.btnChat)
        val _btnAccSetting = findViewById<ImageButton>(R.id.btnAccSetting)
        _tvUsername.setText(current_user.userName)

        val _btnBack = findViewById<ImageButton>(R.id.btnBack)
        _btnBack.setOnClickListener {
            val intent = Intent(this@ClientProfActivity, HomeActivity::class.java)
            intent.putExtra("current_user", current_user)
            startActivity(intent)
        }

        _btnAccSetting.setOnClickListener {
            val intent = Intent(this@ClientProfActivity, ProfileSettingActivity::class.java)
            intent.putExtra("current_user", current_user)
            startActivity(intent)
        }
    }

    fun readProfileImage() {
        val storageRef = FirebaseStorage.getInstance().reference
        val profileRef = storageRef.child("profileImage/" + current_user.profileImage)
        val ONE_MEGABYTE: Long = 1024 * 1024
        profileRef.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener {
                var bmp : Bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                _ivProfile.setImageBitmap(bmp)
            }
    }
}