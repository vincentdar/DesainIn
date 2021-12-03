package com.example.cobachatapp.Helper

import android.util.Log
import java.util.*

object GenerateUUID {
    fun generate() : String {
        val uuid = UUID.randomUUID()
        val str = uuid.toString()
        Log.d("Create UUID", str)

        return str
    }
}