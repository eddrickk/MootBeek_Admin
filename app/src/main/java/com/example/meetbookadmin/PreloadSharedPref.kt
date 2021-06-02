package com.example.meetbookadmin

import android.content.Context
import android.content.SharedPreferences

class PreloadSharedPref(context: Context) {
    private val keyPref = "PRE_LOAD"
    private var mySharePref : SharedPreferences =
            context.getSharedPreferences("SharePrefFile", Context.MODE_PRIVATE)
    var firstRun : Boolean
        get() = mySharePref.getBoolean(keyPref, true)
        set(value) { mySharePref.edit().putBoolean(keyPref,value).commit() }
}