package com.example.cobachatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.NullPointerException

//email: email baca
//password: 123456

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        val _etName = findViewById<EditText>(R.id.etName)
        val _etEmail = findViewById<EditText>(R.id.etEmail)
        val _etPassword = findViewById<EditText>(R.id.etPassword)
        val _etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)

        val _btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val _btnLogin = findViewById<Button>(R.id.btnLogin)


        _btnSignUp.setOnClickListener {
            val userName = _etName.text.toString()
            val email = _etEmail.text.toString()
            val password = _etPassword.text.toString()
            val confirmPassword = _etConfirmPassword.text.toString()

            var check:Int = 0;
            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(applicationContext, "username is required", Toast.LENGTH_SHORT).show()
                check += 1
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "email is required", Toast.LENGTH_SHORT).show()
                check += 1
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "password is required", Toast.LENGTH_SHORT).show()
                check += 1
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(applicationContext, "confirm password is required", Toast.LENGTH_SHORT).show()
                check += 1
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(applicationContext, "password not match", Toast.LENGTH_SHORT).show()
                check += 1
            }

            if (check == 0) {
                _etName.setText("")
                _etEmail.setText("")
                _etPassword.setText("")
                _etConfirmPassword.setText("")
                registerUser(userName, email, password)
            }
            else {
                Toast.makeText(applicationContext, "validation failed", Toast.LENGTH_SHORT).show()
            }
        }

        _btnLogin.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun registerUser(userName:String, email:String, password:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val user:FirebaseUser? = auth.currentUser
                    val userId:String = user!!.uid

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val hashMap:HashMap<String, String> = HashMap()
                    hashMap.put("userId", userId)
                    hashMap.put("userName", userName)
                    hashMap.put("profileImage", "")
                    hashMap.put("desainer", "0")

                    // Set Display name
                    val profile_updates = UserProfileChangeRequest.Builder().setDisplayName(userName).build()
                    user!!.updateProfile(profile_updates)

                    databaseReference.setValue(hashMap).addOnCompleteListener(this) {
                        if (it.isSuccessful) {

                            //open home activity
                            val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                            startActivity(intent)
                        }
                        else {
                            Toast.makeText(applicationContext, "Failed to create database reference", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                else {
                    Toast.makeText(applicationContext, "Failed to create user", Toast.LENGTH_LONG).show()
                }
            }
    }
}