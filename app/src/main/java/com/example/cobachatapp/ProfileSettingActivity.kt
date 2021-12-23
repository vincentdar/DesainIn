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



        val mFragmentManager = supportFragmentManager
        val mfSettings = Settings()

        mFragmentManager.findFragmentByTag(Settings::class.java.simpleName)
        mFragmentManager
            .beginTransaction()
            .add(R.id.frameContainer, mfSettings, Settings::class.java.simpleName)
            .commit()
    }
}


