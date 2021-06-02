package com.example.meetbookadmin

import DB.roomDB
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class roomDBHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "meetbookadmdb.db"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE_ROOM_TABLE = ("CREATE TABLE ${roomDB.roomTable.TABLE_ROOM} " +
                "(${roomDB.roomTable.COLUMN_ID} INTEGER PRIMARY KEY," +
                "${roomDB.roomTable.COLUMN_TITLE} TEXT," +
                "${roomDB.roomTable.COLUMN_CAP} INTEGER," +
                "${roomDB.roomTable.COLUMN_IMAGE} TEXT)")
        db!!.execSQL(CREATE_ROOM_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(
            "DROP TABLE IF EXISTS " +
                    "${roomDB.roomTable.TABLE_ROOM}"
        )
        onCreate(db)
    }
    /*fun getRoom(): MutableList<Room> {
        val roomList : MutableList<Room> = mutableListOf()
        val db = this.readableDatabase
        val projection = arrayOf(roomDB.roomTable.COLUMN_ID,roomDB.roomTable.COLUMN_TITLE,roomDB.roomTable.COLUMN_CAP,roomDB.roomTable.COLUMN_IMAGE)

        var cursor: Cursor? = null
        try {
            cursor = db.query(roomDB.roomTable.TABLE_ROOM,projection,null,null,null,null,null)
        } catch (e: SQLException) {
            //db.execSQL(SELECT_NAME)
            return ArrayList()
        }
        var roomID : Int = 0
        var roomTitle : String = ""
        var roomCap : Int = 0
        var roomImage : String = ""
        if (cursor.moveToFirst()) {
            do {
                roomID = cursor.getInt(
                    cursor.getColumnIndex(roomDB.roomTable.COLUMN_ID)
                )
                roomTitle = cursor.getString(
                    cursor.getColumnIndex(roomDB.roomTable.COLUMN_TITLE)
                )
                roomCap = cursor.getInt(
                    cursor.getColumnIndex(roomDB.roomTable.COLUMN_CAP)
                )
                roomImage = cursor.getString(
                    cursor.getColumnIndex(roomDB.roomTable.COLUMN_IMAGE)
                )

                roomList.add(Room(roomID,roomTitle,roomCap,roomImage))
            } while (cursor.moveToNext())
        }
        return roomList
    }*/
    fun getRoom(): MutableList<Room> {
        val roomList : MutableList<Room> = mutableListOf()
        val SELECT_ROOM = "SELECT *" +
                " FROM ${roomDB.roomTable.TABLE_ROOM}"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(SELECT_ROOM, null)
        } catch (e: SQLException) {
            //db.execSQL(SELECT_NAME)
            return ArrayList()
        }
        var roomID : Int = 0
        var roomTitle : String = ""
        var roomCap : Int = 0
        var roomImage : String = ""
        if (cursor.moveToFirst()) {
            do {
                roomID = cursor.getInt(
                    cursor.getColumnIndex(roomDB.roomTable.COLUMN_ID)
                )
                roomTitle = cursor.getString(
                    cursor.getColumnIndex(roomDB.roomTable.COLUMN_TITLE)
                )
                roomCap = cursor.getInt(
                    cursor.getColumnIndex(roomDB.roomTable.COLUMN_CAP)
                )
                roomImage = cursor.getString(
                    cursor.getColumnIndex(roomDB.roomTable.COLUMN_IMAGE)
                )

                roomList.add(Room(roomID,roomTitle,roomCap,roomImage))
            } while (cursor.moveToNext())
        }
        return roomList
    }
    fun addRoom(room: Room): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(roomDB.roomTable.COLUMN_TITLE, room.title)
            put(roomDB.roomTable.COLUMN_CAP, room.capacity)
            put(roomDB.roomTable.COLUMN_IMAGE, room.image)
        }
        val success = db.insert(
            roomDB.roomTable.TABLE_ROOM,
            null, contentValues
        )
        db.close()
        return success
    }
    fun updateRoom(room: Room): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(roomDB.roomTable.COLUMN_TITLE, room.title)
            put(roomDB.roomTable.COLUMN_CAP, room.capacity)
            put(roomDB.roomTable.COLUMN_IMAGE, room.image)
        }
        var whereArgs = arrayOf(room.id.toString())
        val success = db.update(roomDB.roomTable.TABLE_ROOM,contentValues,"${roomDB.roomTable.COLUMN_ID}=?",whereArgs)
        db.close()
        return success
    }
    fun deleteRoom(id: Int){
        val db = this.writableDatabase
        val selection = "${roomDB.roomTable.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        db.delete(roomDB.roomTable.TABLE_ROOM,selection,selectionArgs)
    }
    fun deleteAll(){
        var db = this.writableDatabase
        db.delete(roomDB.roomTable.TABLE_ROOM,null,null)
    }

    // Buat fungsi begin, success, dan end transaction
    fun beginRoomTransaction(){
        this.writableDatabase.beginTransaction()
    }
    fun successRoomTransaction(){
        this.writableDatabase.setTransactionSuccessful()
    }
    fun endRoomTransaction(){
        this.writableDatabase.endTransaction()
    }

    // Buat fungsi transactional untuk add room
    fun addRoomTransaction(room: Room){
        // Buat query untuk insert room ke table
        var sqlString = "INSERT INTO ${roomDB.roomTable.TABLE_ROOM} " +
                "(${roomDB.roomTable.COLUMN_TITLE}" +
                ",${roomDB.roomTable.COLUMN_CAP}" +
                ",${roomDB.roomTable.COLUMN_IMAGE}) VALUES (?,?,?)"
        // compile statement query
        val statement = this.writableDatabase.compileStatement(sqlString)
        // gunakan bind untuk mengisi values
        // value ke 1 berisi title
        statement.bindString(1, room.title)
        // value ke 2 berisi capacity
        statement.bindLong(2, room.capacity.toLong())
        // value ke 3 berisi image
        statement.bindString(3,room.image)
        // jalankan statement
        statement.execute()
        // bersihkan binding
        statement.clearBindings()
    }

    // Buat fungsi transactional untuk update room
    fun updateRoomTransaction(room: Room){
        // Buat query untuk update data room
        var sqlString = "UPDATE ${roomDB.roomTable.TABLE_ROOM} SET " +
                "${roomDB.roomTable.COLUMN_TITLE} = ?," +
                "${roomDB.roomTable.COLUMN_CAP} = ?," +
                "${roomDB.roomTable.COLUMN_IMAGE} = ? WHERE ${roomDB.roomTable.COLUMN_ID}=?"
        // compile statement query
        val statement = this.writableDatabase.compileStatement(sqlString)
        // gunakan bind untuk mengisi values
        // value ke 1 berisi title untuk set COLUMN_TITLE
        statement.bindString(1, room.title)
        // value ke 2 berisi capacity untuk set COLUMN_CAP
        statement.bindLong(2, room.capacity.toLong())
        // value ke 3 berisi image untuk set COLUMN_IMAGE
        statement.bindString(3,room.image)
        // value ke 4 berisi id untuk where argument
        statement.bindLong(4,room.id.toLong())
        // jalankan statement
        statement.execute()
        // bersihkan bindings
        statement.clearBindings()
    }
    // Buat fungsi untuk read data room dengan mengembalikan List Room yang telah dibaca
    fun getRoomTransaction(): MutableList<Room>{
        val roomList : MutableList<Room> = mutableListOf()
        // Buat query untuk select semua data dari table room
        val SELECT_ROOM = "SELECT *" +
                " FROM ${roomDB.roomTable.TABLE_ROOM}"
        val db = this.readableDatabase
        // baca query select dan masukkan ke dalam cursor
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(SELECT_ROOM, null)
        } catch (e: SQLException) {
            //db.execSQL(SELECT_ROOM)
            return ArrayList()
        }
        var roomID : Int = 0
        var roomTitle : String = ""
        var roomCap : Int = 0
        var roomImage : String = ""
        // Baca cursor dari awal
        if (cursor.moveToFirst()) {
            // Ambil data dari cursor sesuai column dalam table room
            do {
                // Baca column ID
                roomID = cursor.getInt(
                        cursor.getColumnIndex(roomDB.roomTable.COLUMN_ID)
                )
                // Baca column title
                roomTitle = cursor.getString(
                        cursor.getColumnIndex(roomDB.roomTable.COLUMN_TITLE)
                )
                // Baca column capacity
                roomCap = cursor.getInt(
                        cursor.getColumnIndex(roomDB.roomTable.COLUMN_CAP)
                )
                // Baca column image
                roomImage = cursor.getString(
                        cursor.getColumnIndex(roomDB.roomTable.COLUMN_IMAGE)
                )
                // Tambahkan data-data yang telah dibaca ke list Room
                roomList.add(Room(roomID,roomTitle,roomCap,roomImage))
            } while (cursor.moveToNext()) // Lanjut ke data selanjutnya dalam cursor
        }
        // Kembalikan list Room
        return roomList
    }
}