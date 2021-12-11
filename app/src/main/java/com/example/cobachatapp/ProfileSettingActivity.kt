package com.example.cobachatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.lang.NullPointerException

class ProfileSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_profile_setting)

        var current_user = intent.getParcelableExtra<User>("current_user")

        val mFragmentManager = supportFragmentManager
        val mfProfilePicture = ProfilePicture()

        val mBundle = Bundle()
        mBundle.putParcelable("current_user", current_user)
        mfProfilePicture.arguments = mBundle

        mFragmentManager.findFragmentByTag(ProfilePicture::class.java.simpleName)
        mFragmentManager
            .beginTransaction()
            .add(R.id.frameContainer, mfProfilePicture, ProfilePicture::class.java.simpleName)
            .commit()
    }
}


