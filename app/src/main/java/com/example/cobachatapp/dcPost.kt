package com.example.cobachatapp

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class dcPost(
    var userId: String? = null,
    var caption: String? = null,
    var tanggal: String? = null,
) : Parcelable