package com.example.meetbookadmin

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import java.io.ByteArrayOutputStream

//Buat Presenter dengan View dari MainVPInterface
class MainPresenter (setView : MainVPInterface) {
    private var view = setView
    private var mainModel = MainModel()
    private lateinit var imageStr : String

    fun addRoom(nama: String, capacity: String){
        var TempRoom = mainModel.item
        TempRoom.add(Room(nama,capacity.toInt(),imageStr))
        mainModel.item = checkCapacity(capacity,TempRoom)
        mainModel.count = mainModel.item.count()
        view.addRoom(mainModel)
    }

    fun initItems(resources: Resources){
        var imageString = initImage(resources)
        mainModel.item.add(Room("Room 1A", 8, imageString))
        mainModel.count = mainModel.item.count()
        view.init(mainModel)
    }

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

        imageStr = imageString
        return imageString
    }

    fun checkCapacity(capacity: String, tempRoom: MutableList<Room>): MutableList<Room>{
        if (capacity.toInt() <= 0){
            tempRoom.removeLast()
            return tempRoom
        }
        else{
            return tempRoom
        }
    }
}