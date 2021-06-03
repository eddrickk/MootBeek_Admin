package com.example.meetbookadmin

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream

//Buat Presenter dengan View dari MainVPInterface
class MainPresenter (setView : MainVPInterface, context: Context) {
    // Inisialisasi view untuk dapat diakses
    private var view = setView
    // Inisialisasi Model yang akan digunakan
    private var mainModel = MainModel()
    // String yang akan digunakan untuk gambar ruangan
    private lateinit var imageStr : String
    private var context = context

    var roomsqlitedb : roomDBHelper? = null
    // Fungsi untuk menambah ruangan
    fun addRoom(nama: String, capacity: String){
        // Buat sebuah list sementara untuk menampung list saat ini
        //var TempRoom = mainModel.item
        // Tambah List tersebut dengan room yang akan dibuat
        //TempRoom.add(Room(0,nama,capacity.toInt(),imageStr))
        // Ubah kembali model item dengan hasil dari fungsi checkCapacity
        //mainModel.item = checkCapacity(capacity,TempRoom)

        var data = Room(0,nama,capacity.toInt())
        if (capacity.toInt() > 0){
            if (addRoomItems(data) != -1L){
                var result = readRoomItems()
                mainModel.item = result
                // Mengubah total ruangan sesuai jumlah list ruangan
                mainModel.count = mainModel.item.count()
            }
        }
        // Jalankan fungsi addRoom pada view
        view.addRoom(mainModel)
    }

    // Fungsi untuk inisialisasi awal List
    fun initItems(resources: Resources){
        // Generate string gambar
        //var imageString = initImage(resources)
        // Tambahkan Ruangan awal untuk list ruangan
        initRoomItems(resources)
        var roomList = readRoomItems()
        for (data in roomList){
            mainModel.item.add(data)
        }

        // Update jumlah ruangan
        mainModel.count = mainModel.item.count()
        // Jalan fungsi init pada view
        view.init(mainModel)
    }

    fun readRoomItems() : MutableList<Rooms>{
        var roomList : MutableList<Rooms> = mutableListOf()
        roomsqlitedb = roomDBHelper(context)
        doAsync {
            roomList = roomsqlitedb?.getRoom()!!.toMutableList()
        }
        return roomList
    }

    fun addRoomItems(data: Room) : Long?{
        var result : Long? = null
        doAsync {
            val roomTmp = Rooms()
            roomTmp.title = data.title
            roomTmp.capacity = data.capacity
            roomTmp.image = data.image
            //result = roomsqlitedb?.addRoom(roomTmp)
        }
        return result
    }

    fun initRoomItems(resources: Resources){
        var imageString = initImage(resources)
        var roomList : MutableList<Room> = mutableListOf(
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
        roomsqlitedb = roomDBHelper(context)
        // Tambahkan Ruangan awal untuk list ruangan
        doAsync {
            for (data in roomList){
                val roomTmp = Rooms()
                roomTmp.title = data.title
                roomTmp.capacity = data.capacity
                roomTmp.image = data.image
                //var result = roomsqlitedb?.addRoom(roomTmp)
            }
        }
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