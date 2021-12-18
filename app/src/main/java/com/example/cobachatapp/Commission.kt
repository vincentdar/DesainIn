package com.example.cobachatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedemo.CommissionAdapter
import com.google.firebase.firestore.FirebaseFirestore

class Commission : AppCompatActivity() {

    //rv data
    lateinit var rvNotes: RecyclerView
    val title = mutableListOf<String>()
    val client_id = mutableListOf<String>()
    val client_name = mutableListOf<String>()
    val designer_id = mutableListOf<String>()
    val designer_name = mutableListOf<String>()
    val desc = mutableListOf<String>()
    val date = mutableListOf<String>()
    val dc_comm = ArrayList<dcCommission>()

    //database
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commission)

        rvNotes = findViewById(R.id.rv_commission)
        ReadDataComm()

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
        db.collection("tbCommission").get()
            .addOnSuccessListener { result_comm ->
                db.collection("tbUsers").get()
                    .addOnSuccessListener { result_user ->
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
                            val doc_title = doc_comm.data.get("Commission").toString()
                            val doc_client_id = doc_comm.data.get("ClientId").toString()
                            val doc_designer_id = doc_comm.data.get("DesignerId").toString()
                            val doc_desc = doc_comm.data.get("Desc").toString()
                            val doc_date = doc_comm.data.get("Date").toString()
                            val doc_designer_name = "no one"
                            for (doc_user in result_user) {
                                Log.d("comm_firebase", "finding username")
                                if(doc_client_id == doc_user.data.get("userId").toString()) {
                                    val doc_client_name = doc_user.data.get("userName").toString()
                                    client_name.add(doc_client_name)
                                }
                                if(doc_designer_id == doc_user.data.get("userId").toString()){
                                    val doc_designer_name = doc_user.data.get("userName").toString()
                                    designer_name.add(doc_designer_name)
                                }
                            }
                            if(doc_designer_name == "no one"){
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
                    .addOnFailureListener{
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
    }
}