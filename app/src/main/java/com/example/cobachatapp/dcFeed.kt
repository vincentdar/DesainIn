package com.example.cobachatapp

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
//feed
@Parcelize
class dcFeed(
    var feed_user_id: String? = null,
    var feed_username: String? = null,
    var feed_pic: String? = null,
    var feed_caption: String? = null,
    var feed_date: String? = null,
) : Parcelable