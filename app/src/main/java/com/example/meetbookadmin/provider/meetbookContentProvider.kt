package com.example.meetbookadmin.provider

import DB.roomDB
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import com.example.meetbookadmin.roomDBHelper

// Buat content provider untuk room
class meetbookContentProvider : ContentProvider() {
    // Deklarasi dbhelper untuk room dan panggil ketika class di create
    private var dbHelper : roomDBHelper? = null
    override fun onCreate(): Boolean {
        dbHelper = roomDBHelper(context!!)
        return true
    }

    // override fungsi query untuk membaca data room
    override fun query(uri: Uri,
                       projection: Array<out String>?,
                       selection: String?,
                       selectionArgs: Array<out String>?,
                       sortOrder: String?): Cursor? {
        // inisialisasi query builder dan masukkan table yang akan di query
        var queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = roomDB.roomTable.TABLE_ROOM
        // Kembalikan hasil query yang dijalankan ke cursor
        // Masukkan database, data yang diselect, kondisi, isi kondisi, groupby, having dan sort order
        var cursor : Cursor = queryBuilder.query(dbHelper?.readableDatabase,projection,selection,selectionArgs,null,null,sortOrder)
        // panggil setNotificationUri untuk menotifikasi bahwa ada perubahan dengan mengirimnya ke content resolver (untuk diterima di aplikasi lain)
        cursor.setNotificationUri(context?.contentResolver,uri)
        // return cursor
        return cursor
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }
    companion object{
        // Tentukan authority (kepemilikan), table yang boleh diakses, dan content uri untuk menghubungkan dengan aplikasi lain
        val AUTHORITY = "com.example.meetbookadmin.provider.provider.meetbookContentProvider"
        val ROOM_TABLE = roomDB.roomTable.TABLE_ROOM
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/$ROOM_TABLE")
    }
}