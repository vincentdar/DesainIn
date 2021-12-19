package com.example.cobachatapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.viewpager2.widget.ViewPager2
import com.example.cobachatapp.Helper.Dump
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.NullPointerException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.net.URL

class DesainerProfActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var current_user: User
    lateinit var desainer_user: User

    lateinit var _ivProfile: ImageView
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2

    lateinit var _btnFollow: Button
    var followed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_desainer_profile_page)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Get current user data, make sure the home activity has filtered the null user data
        current_user = StaticHolder.get_current_user()
        desainer_user = intent.getParcelableExtra("desainer")!!


        _btnFollow = findViewById<Button>(R.id.btnFollow)
        val _tvFollower = findViewById<TextView>(R.id.tvFollower)
        val _btnAccSetting = findViewById<ImageButton>(R.id.btnAccSetting)

        var tabs = 1;
        var authenticated = false
        var passing_user = current_user
        auth.currentUser?.uid?.let { Log.d("Authentication", it) }
        if (current_user.userId == desainer_user.userId) {
            passing_user = current_user
            _btnFollow.isInvisible = true
            _tvFollower.isInvisible = false
            _btnAccSetting.isGone = false
            authenticated = true
            tabs = 2
        }
        else {
            passing_user = desainer_user
            _btnFollow.isInvisible = false
            _tvFollower.isInvisible = true
            _btnAccSetting.isGone = true
            authenticated = false
            tabs = 1
        }

        // Upper UI Handler
        _ivProfile = findViewById<ImageView>(R.id.ivProfile)
        readProfileImage()

        val _tvUsername = findViewById<TextView>(R.id.tvUsername)
        _tvUsername.setText(passing_user.userName)

        val _btnBack = findViewById<ImageButton>(R.id.btnBack)
        val _btnChat = findViewById<ImageButton>(R.id.btnChat)

        _btnBack.setOnClickListener {
            val intent = Intent(this@DesainerProfActivity, Feed::class.java)
            intent.putExtra("current_user", current_user)
            startActivity(intent)
        }

        _btnAccSetting.setOnClickListener {
            val intent = Intent(this@DesainerProfActivity, ProfileSettingActivity::class.java)
            intent.putExtra("current_user", current_user)
            startActivity(intent)
        }

        _btnChat.setOnClickListener {
            val intent = Intent(this@DesainerProfActivity, GalleryActivity::class.java)
            startActivity(intent)
        }

//        CheckFollower(current_user, desainer_user)
        _btnFollow.setOnClickListener {
            if (followed) {

            }
            else {
                follow(current_user, desainer_user)
            }

        }

        // Lower UI Handler
        tabLayout = findViewById(R.id.tabLayout)
        viewPager2 = findViewById(R.id.viewPager2)

        val adapter = TabProfileAdapter( supportFragmentManager, lifecycle, passing_user, tabs, authenticated)
        viewPager2.adapter = adapter

        val optionsArray = arrayOf("Post", "Upload")
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = optionsArray[position]
        }.attach()

    }

    fun readProfileImage() {
        val storageRef = FirebaseStorage.getInstance().reference
        val profileRef = storageRef.child("profileImage/" + desainer_user.profileImage)
        val ONE_MEGABYTE: Long = 1024 * 1024
        profileRef.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener {
                var bmp : Bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                _ivProfile.setImageBitmap(bmp)
            }
    }

    fun follow(client: User, desainer: User) {
        var dump_desainer = Dump(desainer.userId)
        var dump_client = Dump(client.userId)

        firestore.collection("tbFollowing").document(client.userId.toString()).collection("users").document(desainer.userId.toString()).set(dump_desainer)
            .addOnSuccessListener {
                Log.d("Following", "Berhasil tbFollowing")
//                CheckFollower(client, desainer)
                MotionToast.createColorToast(this, "Follow",
                    "Berhasil Follow",
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.gilroy_light))
            }
            .addOnFailureListener {
                Log.d("Following", "Gagal tbFollowing")
            }

        firestore.collection("tbFollower").document(desainer.userId.toString()).collection("users").document(client.userId.toString()).set(dump_client)
            .addOnSuccessListener {
                Log.d("Following", "Berhasil tbFollower")
            }
            .addOnFailureListener {
                Log.d("Following", "Gagal tbFollower")
            }
    }

//    fun CheckFollower(client: User, desainer: User) : Boolean {
//        firestore.collection("tbFollowing").document(desainer.userId.toString()).collection("users").document(client.userId.toString()).get()
//            .addOnSuccessListener {
//                _btnFollow.setText("Followed")
//                _btnFollow.setBackgroundColor(Color.parseColor("#808080"))
//            }
//            .addOnFailureListener {
//
//            }
//    }

}