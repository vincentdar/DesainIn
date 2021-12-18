package com.example.cobachatapp

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class dcCommission(
    var comm_title: String? = null,
    var comm_client_id: String? = null,
    var comm_client_name: String? = null,
    var comm_designer_id: String? = null,
    var comm_designer_name: String? = null,
    var comm_desc: String? = null,
    var comm_date: String? = null,
) : Parcelable