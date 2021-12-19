package com.example.cobachatapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class dcAddComm (
    var ClientId: String? = null,
    var Commission: String? = null,
    var Date: String? = null,
    var Desc: String? = null,
    var DesignerId: String? = null
) : Parcelable