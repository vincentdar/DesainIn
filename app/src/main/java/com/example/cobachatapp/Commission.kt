package com.example.cobachatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedemo.CommissionAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class Commission : AppCompatActivity() {

    //rv data
    lateinit var rvNotes: RecyclerView
    val comm_id = mutableListOf<String>()
    val title = mutableListOf<String>()
    val client_id = mutableListOf<String>()
    val client_name = mutableListOf<String>()
    val designer_id = mutableListOf<String>()
    val designer_name = mutableListOf<String>()
    val desc = mutableListOf<String>()
    val date = mutableListOf<String>()
    val dc_comm = ArrayList<dcCommission>()

    private lateinit var _progressBarCircular: ProgressBar

    //database
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commission)

        rvNotes = findViewById(R.id.rv_commission)
        ReadDataComm()

        _progressBarCircular = findViewById(R.id.progress_bar_comm)

        val btn_to_add_comm = findViewById<FloatingActionButton>(R.id.fab_add_commission)
        btn_to_add_comm.setOnClickListener {
            if (TextUtils.isEmpty(StaticHolder.get_current_user().userId.toString())) {
                MotionToast.createColorToast(
                    this, "Guest Can't Make Commission",
                    "Please Login",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.gilroy_light)
                )
            } else {
                val intent = Intent(this@Commission, AddCommission::class.java)
                startActivity(intent)
            }

        }

    }

    fun CommToDataClass() {
        for (position in title.indices) {
            val data = dcCommission(
                comm_id[position],
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
        db.collection("tbCommission").get()
            .addOnSuccessListener { result_comm ->
                db.collection("tbUsers").get()
                    .addOnSuccessListener { result_user ->
                        if(result_comm.size() == 0){
                            _progressBarCircular.isGone = true

                        }
                        Log.d("comm_firebase", "listening")
                        comm_id.clear()
                        title.clear()
                        client_id.clear()
                        client_name.clear()
                        designer_id.clear()
                        designer_name.clear()
                        desc.clear()
                        date.clear()
                        dc_comm.clear()
                        for (doc_comm in result_comm) {
                            val doc_id = doc_comm.id
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
                            comm_id.add(doc_id)
                            title.add(doc_title)
                            client_id.add(doc_client_id)
                            designer_id.add(doc_designer_id)
                            desc.add(doc_desc)
                            date.add(doc_date)
                        }
                        _progressBarCircular.isGone = true
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
        rvNotes.layoutManager = GridLayoutManager(this, 2)
        val notesAdapter = CommissionAdapter(dc_comm)
        rvNotes.adapter = notesAdapter

        notesAdapter.setOnItemClickCallback(object : CommissionAdapter.OnItemClickCallback{
            override fun OnBodyClicked(data: dcCommission) {
                Log.d("comm_click", "OnBodyClick is clicked")
                Log.d("comm_click", data.comm_id.toString())
                val intent = Intent(this@Commission, CommissionDetail::class.java)
                intent.putExtra("CommissionDetail", data)
                startActivity(intent)
                finish()
            }
        })
    }

}