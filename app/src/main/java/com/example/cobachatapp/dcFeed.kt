package com.example.cobachatapp

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class dcFeed(
    var feed_user_id: String? = null,
    var feed_caption: String? = null,
    var feed_date: String? = null,
) : Parcelable