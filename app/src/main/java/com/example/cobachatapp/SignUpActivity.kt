package com.example.cobachatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import com.example.cobachatapp.Helper.Dump
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.NullPointerException


class SignUpActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

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
                // Toast.makeText(applicationContext, "username is required", Toast.LENGTH_SHORT).show()
                MotionToast.createColorToast(this, "Invalid Credentials",
                    "Username is required",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.gilroy_light))
                check += 1
            }
            if (TextUtils.isEmpty(email)) {
                // Toast.makeText(applicationContext, "email is required", Toast.LENGTH_SHORT).show()
                MotionToast.createColorToast(this, "Invalid Credentials",
                    "Email is required",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.gilroy_light))
                check += 1
            }
            if (TextUtils.isEmpty(password)) {
                // Toast.makeText(applicationContext, "password is required", Toast.LENGTH_SHORT).show()
                MotionToast.createColorToast(this, "Invalid Credentials",
                    "Password is required",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.gilroy_light))
                check += 1
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                // Toast.makeText(applicationContext, "confirm password is required", Toast.LENGTH_SHORT).show()
                MotionToast.createColorToast(this, "Invalid Credentials",
                    "Confirm password is required",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.gilroy_light))
                check += 1
            }

            if (!password.equals(confirmPassword)) {
                // Toast.makeText(applicationContext, "password not match", Toast.LENGTH_SHORT).show()
                MotionToast.createColorToast(this, "Invalid Credentials",
                    "Password not match",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.gilroy_light))
                check += 1
            }

            if (check == 0) {
                _etName.setText("")
                _etEmail.setText("")
                _etPassword.setText("")
                _etConfirmPassword.setText("")
                registerUser(userName, email, password)
            }
        }

        _btnLogin.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    //for realtime database only
    private fun addUsertoDatabase(userName:String, email:String, uid: String){
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(UserForRealtime(userName, email, uid))
    }

    private fun registerUser(userName:String, email:String, password:String) {
        firestore.collection("tbUsers").get()
            .addOnSuccessListener {
                var usernameAvailable = true
                for (document in it) {
                    if (document.get("userName").toString() == userName) {
                        usernameAvailable = false
                        break
                    }
                }
                if (usernameAvailable) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) {
                            if (it.isSuccessful) {
                                val user:FirebaseUser? = auth.currentUser
                                val userId:String = user!!.uid

                                val data = User(userName, "", userId, "0")

                                //insert to realtime database
                                addUsertoDatabase(userName, email, user.uid!!)
                                // Set Display name
                                val profile_updates = UserProfileChangeRequest.Builder().setDisplayName(userName).build()
                                user!!.updateProfile(profile_updates)

                                var dump = Dump(userId)

                                firestore.collection("tbUsers").document(userId)
                                    .set(data)
                                    .addOnSuccessListener {
                                        firestore.collection("tbFollowing").document(userId).set(dump)
                                            .addOnSuccessListener {
                                                firestore.collection("tbFollowing").document(userId).collection("users").document(userId).set(dump)
                                                    .addOnSuccessListener {
                                                        Log.d("Firestore", "Save data success")
                                                    }
                                                    .addOnFailureListener {
                                                        Log.d("Firestore", "Save data failed")
                                                    }
                                            }
                                            .addOnFailureListener {
                                                Log.d("Firestore", "Save data failed")
                                            }

                                        firestore.collection("tbFollower").document(userId).set(dump)
                                            .addOnSuccessListener {
                                                firestore.collection("tbFollower").document(userId).collection("users").document(userId).set(dump)
                                                    .addOnSuccessListener {
                                                        Log.d("Firestore", "Save data success")
                                                    }
                                                    .addOnFailureListener {
                                                        Log.d("Firestore", "Save data failed")
                                                    }
                                            }
                                            .addOnFailureListener {
                                                Log.d("Firestore", "Save data failed")
                                            }

                                        StaticHolder.set_current_user(data)

                                        val intent = Intent(this@SignUpActivity, Feed::class.java)
                                        startActivity(intent)

                                        Log.d("Firestore", "Save data success")
                                    }
                                    .addOnFailureListener {
                                        Log.d("Firestore", "Save data failed")
                                    }


                            }
                            else {
                                Log.d("Auth", "Failed to create user")
                                MotionToast.createColorToast(this, "Error",
                                    "Failed to create user",
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.SHORT_DURATION,
                                    ResourcesCompat.getFont(this, R.font.gilroy_light))
                            }
                        }
                }
                else {
                    Log.d("Auth", "Username already exists")
                    MotionToast.createColorToast(this, "Error",
                        "Username already exists",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(this, R.font.gilroy_light))
                }
            }
            .addOnFailureListener {
                Log.d("Firestore", "Failure to get users")
                MotionToast.createColorToast(this, "Error",
                    "Firestore failure",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.gilroy_light))
            }


    }
}