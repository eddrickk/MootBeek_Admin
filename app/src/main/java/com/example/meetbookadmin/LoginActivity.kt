package com.example.meetbookadmin

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream

class LoginActivity : AppCompatActivity() {
    val user = "Admin"
    val pass = "123"
    // inisialisasi Db helper untuk room
    var roomsqlitedb : roomDBHelper? = null
    private var imageString : String = ""
    // Buat data dummy untuk dimasukkan ke database
    var roomList = listOf(
        Room(0, "Room 1A",1,imageString),
        Room(1, "Room 2A",1,imageString),
        Room(2, "Room 3A",1,imageString),
        Room(3, "Room 4A",1,imageString),
        Room(4, "Room 5A",1,imageString),
        Room(5, "Room 6A",1,imageString),
        Room(6, "Room 7A",1,imageString),
        Room(7, "Room 8A",1,imageString),
        Room(8, "Room 9A",1,imageString),
        Room(9, "Room 1B",1,imageString),
        Room(10, "Room 2B",1,imageString),
        Room(11, "Room 3B",1,imageString),
        Room(12, "Room 4B",1,imageString),
        Room(13, "Room 5B",1,imageString),
        Room(14, "Room 6B",1,imageString),
        Room(15, "Room 7B",1,imageString),
        Room(16, "Room 8B",1,imageString),
        Room(17, "Room 9B",1,imageString)
    )
    // inisialisasi shared preferences untuk preload
    var myFirstRunSharePref : PreloadSharedPref? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // panggil db helper
        roomsqlitedb = roomDBHelper(this)
        // panggil sharedpref
        myFirstRunSharePref = PreloadSharedPref(this)
        //roomsqlitedb?.deleteAll()
        //myFirstRunSharePref?.firstRun = true

        // Jika isi sharedpreferences adalah true (database belum pernah di load)
        if(myFirstRunSharePref!!.firstRun){
            // Jalankan fungsi transaction untuk add room
            additemTransaction()
        }

        LoginButtonAdm.setOnClickListener {
            var intentToMain = Intent(this, MainActivity::class.java)

            if (AuthenticateUser(LUsernameAdm.text.toString()))
            {
                if (AuthenticatePass(LPasswordAdm.text.toString()))
                {
                    startActivity(intentToMain)
                }
                else{
                    makeToast("Password Tidak Cocok")
                }
            }
            else{
                makeToast("Username Tidak Cocok")
            }
        }
    }

    // Buat fungsi untuk add room
    private fun additemTransaction() {
        // Matikan button untuk login
        LoginButtonAdm.isEnabled = false
        // set progress awal ke 0 dan max ke jumlah data dummy yang akan ditambahkan
        progressbar.progress = 0
        progressbar.max = roomList.size
        doAsync {
            // Mulai transaction
            roomsqlitedb?.beginRoomTransaction()
            // baca setiap data dalam roomlist
            for (data in roomList){
                //var tmp = Rooms()
                //tmp.title = data.title
                //tmp.capacity = data.capacity
                //tmp.image = data.image

                // Jalankan fungsi untuk menambah data dari DB Helper dengan memasukkan data
                roomsqlitedb?.addRoomTransaction(data)
                uiThread {
                    // setiap data yang ditambahkan update progress
                    progressbar.progress += 1
                }
            }
            // nyatakan transaction sukses
            roomsqlitedb?.successRoomTransaction()
            // Transaction selesai
            roomsqlitedb?.endRoomTransaction()
            uiThread {
                // Ubah button untuk kembali bisa ditekan
                LoginButtonAdm.isEnabled = true
                // ubah sharedpreferences menjadi false menandakan data sudah pernah di load
                myFirstRunSharePref?.firstRun = false
            }
        }
    }

    private fun makeToast(text : String) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }

    private fun AuthenticatePass(password: String): Boolean {
        if (password.toLowerCase() == pass.toLowerCase()){
            return true
        }
        return false
    }

    private fun AuthenticateUser(username: String): Boolean {
        if (username.toLowerCase() == user.toLowerCase()){
            return true
        }
        return false
    }
    private fun additem(){
        LoginButtonAdm.isEnabled = false
        progressbar.progress = 0
        progressbar.max = roomList.size
        doAsync {
            for (data in roomList){
                var tmp = Rooms()
                tmp.title = data.title
                tmp.capacity = data.capacity
                tmp.image = data.image
                roomsqlitedb?.addRoom(data)
                uiThread {
                    progressbar.progress += 1
                }
            }
            LoginButtonAdm.isEnabled = true

        }
    }
    fun initImage() : String{
        //Decode Gambar menjadi bitmap dan set image pada imageview
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.meetroom1)
        //meetimage.setImageBitmap(bitmap as Bitmap)

        //Convert Image into Base64 String --> Start
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        var imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        // --> End

        // Masukkan ke variable imageStr untuk dapat digunakan nantinya
        //imageStr = imageString

        //Kembalikan string yang telah di buat
        return imageString
    }
}