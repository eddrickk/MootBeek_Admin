package com.example.meetbookadmin

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Rooms : Parcelable {
    var id : Int = 0
    var title: String = ""
    var capacity: Int = 0
    var image: String = ""
}