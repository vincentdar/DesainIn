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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.NullPointerException

class LoginActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_login)

        val _btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val _btnLogin = findViewById<Button>(R.id.btnLogin)
        val _etEmail = findViewById<EditText>(R.id.etEmail)
        val _etPassword = findViewById<EditText>(R.id.etPassword)

        auth = FirebaseAuth.getInstance()

        _btnLogin.setOnClickListener {
            val email = _etEmail.text.toString()
            val password = _etPassword.text.toString()

            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "email and password are required", Toast.LENGTH_SHORT).show()
            }
            else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(applicationContext, "email and password invalid", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        _btnSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }


}