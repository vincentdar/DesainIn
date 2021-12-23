package com.example.cobachatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.firestore.FirebaseFirestore
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.NullPointerException

class CommissionDetail : AppCompatActivity() {

    val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var commission : dcCommission

    lateinit var client : TextView
    lateinit var designer : TextView
    lateinit var desc : TextView
    lateinit var date : TextView
    lateinit var comm_id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_commission_detail)

        commission = intent.getParcelableExtra<dcCommission>("CommissionDetail")!!

        client = findViewById(R.id.tv_client_name)
        designer = findViewById(R.id.tv_designer_name)
        date = findViewById(R.id.tv_date)
        desc = findViewById(R.id.tv_desc)
        ShowData()
        comm_id = commission.comm_id.toString()

        val btn_cancel = findViewById<Button>(R.id.btn_cancel)
        btn_cancel.setOnClickListener{
            MotionToast.createColorToast(this, "Canceled",
                "Too bad",
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this, R.font.gilroy_light))
            val intent = Intent(this@CommissionDetail, Commission::class.java)
            startActivity(intent)
            finish()
        }
        val btn_accept = findViewById<Button>(R.id.btn_accept)
        btn_accept.setOnClickListener{
            val data = dcAddComm(
                commission.comm_client_id,
                commission.comm_title,
                commission.comm_date,
                commission.comm_desc,
                StaticHolder.get_current_user().userId.toString()
            )
            db.collection("tbCommission").document(comm_id)
                .set(data)
                .addOnSuccessListener {
                    MotionToast.createColorToast(
                        this, "Commission Accepted",
                        "Congratulation!!, you got work to do",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(this, R.font.gilroy_light)
                    )
                    val intent = Intent(this@CommissionDetail, Commission::class.java)
                    startActivity(intent)
                }
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@CommissionDetail, Commission::class.java)
        startActivity(intent)
    }

    fun ShowData() {
        client.setText(commission.comm_client_name)
        designer.setText(commission.comm_designer_name)
        desc.setText(commission.comm_desc)
        date.setText(commission.comm_date)
    }
}