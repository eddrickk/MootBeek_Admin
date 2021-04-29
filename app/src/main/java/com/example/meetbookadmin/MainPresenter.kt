package com.example.meetbookadmin

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import java.io.ByteArrayOutputStream

//Buat Presenter dengan View dari MainVPInterface
class MainPresenter (setView : MainVPInterface) {
    // Inisialisasi view untuk dapat diakses
    private var view = setView
    // Inisialisasi Model yang akan digunakan
    private var mainModel = MainModel()
    // String yang akan digunakan untuk gambar ruangan
    private lateinit var imageStr : String

    // Fungsi untuk menambah ruangan
    fun addRoom(nama: String, capacity: String){
        // Buat sebuah list sementara untuk menampung list saat ini
        var TempRoom = mainModel.item
        // Tambah List tersebut dengan room yang akan dibuat
        TempRoom.add(Room(nama,capacity.toInt(),imageStr))
        // Ubah kembali model item dengan hasil dari fungsi checkCapacity
        mainModel.item = checkCapacity(capacity,TempRoom)
        // Mengubah total ruangan sesuai jumlah list ruangan
        mainModel.count = mainModel.item.count()
        // Jalankan fungsi addRoom pada view
        view.addRoom(mainModel)
    }

    // Fungsi untuk inisialisasi awal List
    fun initItems(resources: Resources){
        // Generate string gambar
        var imageString = initImage(resources)
        // Tambahkan Ruangan awal untuk list ruangan
        mainModel.item.add(Room("Room 1A", 8, imageString))
        // Update jumlah ruangan
        mainModel.count = mainModel.item.count()
        // Jalan fungsi init pada view
        view.init(mainModel)
    }

    // Fungsi untuk generate string gambar
    fun initImage(resources: Resources) : String{
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
        imageStr = imageString

        //Kembalikan string yang telah di buat
        return imageString
    }

    // Fungsi untuk mengecek apabila kapasitas ruangan melebihi 0 atau tidak
    // Parameter : kapasitas ruangan, list sementara yang telah dibuat
    fun checkCapacity(capacity: String, tempRoom: MutableList<Room>): MutableList<Room>{
        // Jika ruangan tidka lebih dari 0 maka list tidak jadi ditambahkan ruangan baru
        if (capacity.toInt() <= 0){
            tempRoom.removeLast()
            return tempRoom
        }
        // Jika tidak, List tetap
        else{
            return tempRoom
        }
    }
}