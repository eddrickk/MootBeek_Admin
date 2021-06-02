package com.example.meetbookadmin

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Room (var id : Int, var title: String = "", var capacity: Int = 0, var image: String = "") : Parcelable {
}