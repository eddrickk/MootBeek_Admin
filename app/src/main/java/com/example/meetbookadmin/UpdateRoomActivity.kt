package com.example.meetbookadmin

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import kotlinx.android.synthetic.main.activity_update_room.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class UpdateRoomActivity : AppCompatActivity() {
    var roomsqlitedb : roomDBHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_room)
        roomsqlitedb = roomDBHelper(this)
        var intentExtra = intent
        doAsync{
            val imageresult=decodeStrImg(intentExtra.getStringExtra(EXTRA_ROOM_IMAGE))
            uiThread {
                MeetRoomImg.setImageBitmap(imageresult)
            }
        }
        MeetRoomTitle.text = intentExtra.getStringExtra(EXTRA_ROOM_TITLE)
        MeetRoomCap.text = "${intentExtra.getIntExtra(EXTRA_ROOM_CAP, 0)} Seats"

        SaveBtn.setOnClickListener {
            // Ambil ID yang dikirim
            var roomID = intentExtra.getIntExtra(EXTRA_ROOM_ID, 0)
            // ambil data yang akan di update
            var updateTitle = editTextTitle.text.toString()
            var updateCap = editTextCap.text.toString().toInt()
            var updateImg = intentExtra.getStringExtra(EXTRA_ROOM_IMAGE) ?: ""

            doAsync {
                // Mulai transaction
                roomsqlitedb?.beginRoomTransaction()
                // Jalankan fungsi untuk update data room dari DB Helper
                roomsqlitedb?.updateRoomTransaction(Room(roomID,updateTitle,updateCap,updateImg))
                // Nyatakan transaction selesai
                roomsqlitedb?.successRoomTransaction()
                // Transaction selesai
                roomsqlitedb?.endRoomTransaction()
                uiThread {
                    // Selesaikan activity
                    finishThisActivity()
                }
            }

            /*intentMain.putExtra(EXTRA_UPDATE_ROOM_ID, roomID)
            intentMain.putExtra(EXTRA_UPDATE_ROOM_TITLE, updateTitle)
            intentMain.putExtra(EXTRA_UPDATE_ROOM_CAP, updateCap)
            intentMain.putExtra(EXTRA_UPDATE_ROOM_IMAGE, updateImg)

            setResult(EXTRA_UPDATE_RESULT, intentMain)*/
            //finish()
        }
    }

    private fun finishThisActivity() {
        this.finish()
    }

    fun decodeStrImg (param3:String?): Bitmap? {
        val imageBytes = Base64.decode(param3, Base64.DEFAULT)
        val decodeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return decodeImage
    }
}