package com.example.cobachatapp

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User (
    var userName:String?,
    var profileImage:String?,
    var userId:String?,
    var desainer:String?
) : Parcelable