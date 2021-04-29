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
        // Jalankan fungsi untuk Inisialisasi awal List
        presenter.initItems(resources)

        // RoomItems yang telah diinisialisasi dimasukkan ke adapter
        RoomListAdminAdapter = RoomListAdminRecyclerViewAdapter(RoomItems)
        // Tentukan adapter dan LayoutManager
        recyclerViewRoomList.adapter = RoomListAdminAdapter
        recyclerViewRoomList.layoutManager = LinearLayoutManager(this)

        // Ketika button add diklik, munculkan Dialog untuk mengisi ruangan baru
        btnAdd.setOnClickListener {
            var BuilderDialog = AlertDialog.Builder(this)
            var inflaterDialog = layoutInflater.inflate(R.layout.dialog_add_room,null)
            BuilderDialog.setView(inflaterDialog)
            BuilderDialog.setPositiveButton("ADD"){ dialogInterface: DialogInterface, i: Int ->
                var NamaRoomBaru = inflaterDialog.findViewById<EditText>(R.id.inputNamaRoom)
                var KapasitasBaru = inflaterDialog.findViewById<EditText>(R.id.inputKapasitas)

                // Jalankan fungsi addRoom di presenter untuk mengatur penambahan ruangan
                presenter.addRoom(NamaRoomBaru.text.toString(),KapasitasBaru.text.toString())
                //var ImageBaru = inflaterDialog.findViewById<ImageView>(R.id.imageRoom)
                //RoomItems.add(Room(NamaRoomBaru.text.toString(),KapasitasBaru.text.toString().toInt(),imageString))
            }
            BuilderDialog.create().show()
        }
    }

    // Fungsi untuk menampilkan ruangan yang telah diupdate
    override fun addRoom(model: MainModel) {
        // RoomItems saat ini dan list terbaru berjumlah sama banyak, maka tidak ada penambahan
        if (RoomItems.size == model.item.size) {
            // Munculkan toast untuk indikasi tidak bisa terjadi penambahan (kapasitas <= 0)
            Toast.makeText(this,"Note Of The Dat : Kapasitas Tidak Boleh Lebih Kecil Dari 0",Toast.LENGTH_SHORT).show()
        }
        // Update RoomItems dan Jumlah
        RoomItems = model.item
        RoomCount.text = "Total Room : ${model.count.toString()}"
        // Notify Adapter recyclerView bahwa ada perubahan pada list
        RoomListAdminAdapter.notifyItemInserted(RoomListAdminAdapter.itemCount)
    }

    // Fungsi untuk mengupdate RoomItems (List) yang akan digunakan pada recyclerView
    override fun init(model: MainModel) {
        RoomItems = model.item
        RoomCount.text = "Total Room : ${model.count.toString()}"
    }
}