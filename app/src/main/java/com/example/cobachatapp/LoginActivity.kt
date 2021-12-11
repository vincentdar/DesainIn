package com.example.cobachatapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.NullPointerException

class LoginActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_login)

        val _btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val _btnLogin = findViewById<Button>(R.id.btnLogin)
        val _btnLoginAsGuest = findViewById<Button>(R.id.btnLoginAsGuest)
        val _etEmail = findViewById<EditText>(R.id.etEmail)
        val _etPassword = findViewById<EditText>(R.id.etPassword)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        _btnLogin.setOnClickListener {
            val email = _etEmail.text.toString()
            val password = _etPassword.text.toString()

            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                // Toast.makeText(applicationContext, "email and password are required", Toast.LENGTH_SHORT).show()
                MotionToast.createColorToast(this, "Invalid Credentials",
                    "Email and Password required",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.gilroy_light))
            }
            else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            val uid = user.uid
                            firestore.collection("tbUsers").document(uid).get()
                                .addOnSuccessListener {
                                    val userName = it.get("userName").toString()
                                    val userId = it.get("userId").toString()
                                    val profileImage = it.get("profileImage").toString()
                                    val desainer = it.get("desainer").toString()
                                    val data = User(userName, profileImage, userId, desainer)
                                    Log.d("Firestore", "Success to get current user data")
                                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                    intent.putExtra("current_user", data)
                                    startActivity(intent)
                                }
                                .addOnFailureListener {
                                    Log.d("Firestore", "Failure to get current user data")
                                    Toast.makeText(applicationContext, "Firestore Failure", Toast.LENGTH_SHORT).show()
                                }

                        }

                    }
                    else {
                        // Toast.makeText(applicationContext, "email and password invalid", Toast.LENGTH_SHORT).show()
                        MotionToast.createColorToast(this, "Invalid Credentials",
                            "Email and password invalid",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(this, R.font.gilroy_light))
                    }
                }
            }
        }

        _btnSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        _btnLoginAsGuest.setOnClickListener {
            auth.signOut()
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
        }
    }


}