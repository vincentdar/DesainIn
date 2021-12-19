package com.example.cobachatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import com.example.cobachatapp.Helper.DateHelper
import com.google.firebase.firestore.FirebaseFirestore
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class AddCommission : AppCompatActivity() {

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var et_title: EditText
    lateinit var et_desc: EditText
    lateinit var btn_send: Button
    lateinit var title: String
    lateinit var desc: String
    lateinit var date: String
    lateinit var data: dcAddComm
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_commission)

        et_title = findViewById<EditText>(R.id.et_title)
        et_desc = findViewById<EditText>(R.id.et_desc)
        btn_send = findViewById<Button>(R.id.btn_commission)



        btn_send.setOnClickListener {
            userId = StaticHolder.get_current_user().userId.toString()
            title = et_title.text.toString()
            desc = et_desc.text.toString()
            date = DateHelper.getCurrentDate()
            data = dcAddComm(userId, title, date, desc, "")
            if (TextUtils.isEmpty(title)) {
                MotionToast.createColorToast(
                    this, "Invalid Input",
                    "Commission Title is required",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.gilroy_light)
                )
            } else if (TextUtils.isEmpty(desc)) {
                MotionToast.createColorToast(
                    this, "Invalid Input",
                    "Commission Description is required",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.gilroy_light)
                )
            } else {
                AddData()
            }
        }

    }

    private fun AddData() {
        db.collection("tbCommission").document()
            .set(data)
            .addOnSuccessListener {
                val intent = Intent(this@AddCommission, Commission::class.java)
                startActivity(intent)
                Log.d("add_comm_firebase", "writing data")
                MotionToast.createColorToast(
                    this, "Commission Added",
                    "Your commission has been added successfully",
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.gilroy_light)
                )
                finish()
            }
            .addOnFailureListener {
                Log.d("add_comm_firebase", it.toString())
                MotionToast.createColorToast(
                    this, "Oops Something Happened",
                    "An error has occurred",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.gilroy_light)
                )
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}