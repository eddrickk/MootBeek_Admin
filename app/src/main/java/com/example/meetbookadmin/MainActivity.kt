package com.example.meetbookadmin

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity(), MainVPInterface {
    // Buat Adapter untuk room list
    private lateinit var RoomListAdminAdapter : RoomListAdminRecyclerViewAdapter
    // Buat List untuk Room yang akan ditampilkan pada recyclerview
    private var RoomItems : MutableList<Room> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi Presenter yang akan digunakan
        var presenter = MainPresenter(this)
        presenter.initItems(resources)

        RoomListAdminAdapter = RoomListAdminRecyclerViewAdapter(RoomItems)
        recyclerViewRoomList.adapter = RoomListAdminAdapter
        recyclerViewRoomList.layoutManager = LinearLayoutManager(this)

        btnAdd.setOnClickListener {
            var BuilderDialog = AlertDialog.Builder(this)
            var inflaterDialog = layoutInflater.inflate(R.layout.dialog_add_room,null)
            BuilderDialog.setView(inflaterDialog)
            BuilderDialog.setPositiveButton("ADD"){ dialogInterface: DialogInterface, i: Int ->
                var NamaRoomBaru = inflaterDialog.findViewById<EditText>(R.id.inputNamaRoom)
                var KapasitasBaru = inflaterDialog.findViewById<EditText>(R.id.inputKapasitas)
                //var ImageBaru = inflaterDialog.findViewById<ImageView>(R.id.imageRoom)
                //RoomItems.add(Room(NamaRoomBaru.text.toString(),KapasitasBaru.text.toString().toInt(),imageString))
                presenter.addRoom(NamaRoomBaru.text.toString(),KapasitasBaru.text.toString())
            }
            BuilderDialog.create().show()
        }
    }

    override fun addRoom(model: MainModel) {
        if (RoomItems.size == model.item.size) {
            Toast.makeText(this,"Kapasitas Tidak Boleh 0 !!",Toast.LENGTH_SHORT).show()
        }
        RoomItems = model.item
        RoomCount.text = model.count.toString()
        RoomListAdminAdapter.notifyItemInserted(RoomListAdminAdapter.itemCount)
    }

    override fun init(model: MainModel) {
        RoomItems = model.item
        RoomCount.text = model.count.toString()
    }
}