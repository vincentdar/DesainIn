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
    lateinit var rvNotes : RecyclerView
    val title = mutableListOf<String>()
    val client_name = mutableListOf<String>()
    val designer_name = mutableListOf<String>()
    val desc = mutableListOf<String>()
    val date = mutableListOf<String>()
    val dc_comm = ArrayList<dcCommission>()

    //database
    private var db:FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commission)

        rvNotes = findViewById(R.id.rv_commission)
        ReadDataComm()

    }

    fun CommToDataClass(){
        for(position in title.indices){
            val data = dcCommission(
                title[position],
                client_name[position],
                designer_name[position],
                desc[position],
                date[position]
            )
            dc_comm.add(data)
        }
    }

    private fun ReadDataComm(){
        db.collection("tbCommission").get()
            .addOnSuccessListener { result ->
                Log.d("comm firebase", "listening")
                title.clear()
                client_name.clear()
                designer_name.clear()
                desc.clear()
                date.clear()
                dc_comm.clear()
                for (doc in result){
                    val doc_title = doc.data.get("Commission").toString()
                    val doc_client_name = doc.data.get("ClientId").toString()
                    val doc_designer_name = doc.data.get("DesignerId").toString()
                    val doc_desc = doc.data.get("Desc").toString()
                    val doc_date = doc.data.get("Date").toString()
                    title.add(doc_title)
                    client_name.add(doc_client_name)
                    designer_name.add(doc_designer_name)
                    desc.add(doc_desc)
                    date.add(doc_date)
                }
                CommToDataClass()
                Log.d("comm firebase", "read data - size : " + result.size())
                ShowData()
            }
            .addOnFailureListener{
                Log.d("comm firebase", it.message.toString())
            }
    }

    fun ShowData(){
        rvNotes.layoutManager = GridLayoutManager(this, 2)
        val notesAdapter = CommissionAdapter(dc_comm)
        rvNotes.adapter = notesAdapter
    }
}