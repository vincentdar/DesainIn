package com.example.cobachatapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedemo.CommissionAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.lang.NullPointerException

class ClientProfActivity : AppCompatActivity() {
    var current_user: User = StaticHolder.get_current_user()
    lateinit var _ivProfile: ImageView
    lateinit var _rvCommisions: RecyclerView

    val title = mutableListOf<String>()
    val client_id = mutableListOf<String>()
    val client_name = mutableListOf<String>()
    val designer_id = mutableListOf<String>()
    val designer_name = mutableListOf<String>()
    val desc = mutableListOf<String>()
    val date = mutableListOf<String>()
    val dc_comm = ArrayList<dcCommission>()

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()



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
            val intent = Intent(this@ClientProfActivity, Feed::class.java)
            intent.putExtra("current_user", current_user)
            startActivity(intent)
        }

        _btnAccSetting.setOnClickListener {
            val intent = Intent(this@ClientProfActivity, ProfileSettingActivity::class.java)
            intent.putExtra("current_user", current_user)
            startActivity(intent)
        }

        _btnChat.setOnClickListener {
            val intent = Intent(this@ClientProfActivity, GalleryActivity::class.java)
            startActivity(intent)
        }

        // Lower UI Handler

        _rvCommisions = findViewById(R.id.rvCommissions)
        ReadDataComm()
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



    fun CommToDataClass() {
        for (position in title.indices) {
            val data = dcCommission(
                title[position],
                client_id[position],
                client_name[position],
                designer_id[position],
                designer_name[position],
                desc[position],
                date[position]
            )
            dc_comm.add(data)
        }
    }

    private fun ReadDataComm() {
        db.collection("tbCommission").whereEqualTo("clientId", current_user.userId).get()
            .addOnSuccessListener { result_comm ->
                db.collection("tbUsers").get()
                    .addOnSuccessListener { result_user ->
                        if(result_comm.size() == 0){
                            // nanti dikerjakan
                        }
                        Log.d("comm_firebase", "listening")
                        title.clear()
                        client_id.clear()
                        client_name.clear()
                        designer_id.clear()
                        designer_name.clear()
                        desc.clear()
                        date.clear()
                        dc_comm.clear()
                        for (doc_comm in result_comm) {
                            val doc_title = doc_comm.data.get("commission").toString()
                            val doc_client_id = doc_comm.data.get("clientId").toString()
                            val doc_designer_id = doc_comm.data.get("designerId").toString()
                            val doc_desc = doc_comm.data.get("desc").toString()
                            val doc_date = doc_comm.data.get("date").toString()
                            val doc_designer_name = "no one"
                            for (doc_user in result_user) {
                                Log.d("comm_firebase", "finding username")
                                if (doc_client_id == doc_user.data.get("userId").toString()) {
                                    val doc_client_name = doc_user.data.get("userName").toString()
                                    client_name.add(doc_client_name)
                                }
                                if (doc_designer_id == doc_user.data.get("userId").toString()) {
                                    val doc_designer_name = doc_user.data.get("userName").toString()
                                    designer_name.add(doc_designer_name)
                                }
                            }
                            if (doc_designer_name == "no one") {
                                designer_name.add("")
                            }
                            title.add(doc_title)
                            client_id.add(doc_client_id)
                            designer_id.add(doc_designer_id)
                            desc.add(doc_desc)
                            date.add(doc_date)
                        }
                        CommToDataClass()
                        Log.d("comm_firebase", "read data - size : " + result_comm.size())
                        ShowData()
                    }
                    .addOnFailureListener {
                        Log.d("comm_firebase", it.toString())
                    }
            }
            .addOnFailureListener {
                Log.d("comm_firebase", it.message.toString())
            }
    }

    fun ShowData() {
        _rvCommisions.layoutManager = GridLayoutManager(this, 2)
        val notesAdapter = CommissionAdapter(dc_comm)
        _rvCommisions.adapter = notesAdapter
    }
}