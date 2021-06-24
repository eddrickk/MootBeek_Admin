package com.example.meetbookadmin

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity(), InterfaceData/*, MainVPInterface*/ {
    // Inisialisasi db helper untuk room
    var roomsqlitedb : roomDBHelper? = null
    //lateinit var controller: FirebaseController
    private lateinit var imageStr : String
    // Buat Adapter untuk room list
    private lateinit var RoomListAdminAdapter : RoomListAdminRecyclerViewAdapter
    private lateinit var interfaceData: InterfaceData
    // Buat List untuk Room yang akan ditampilkan pada recyclerview
    private var RoomItems : MutableList<Rooms> = mutableListOf(

    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*controller = FirebaseController(this)
        controller.getRoomList()*/
        // panggil db Helper
        roomsqlitedb = roomDBHelper(this)
        /*doAsync {
            roomsqlitedb?.beginRoomTransaction()
            RoomItems = roomsqlitedb?.getRoomTransaction()!!
            roomsqlitedb?.successRoomTransaction()
            roomsqlitedb?.endRoomTransaction()
        }*/
        // jalankan fungsi readData()
        readData()
        initImage()
        interfaceData = this as InterfaceData
        // Inisialisasi Presenter yang akan digunakan
        //var presenter = MainPresenter(this, this)
        // Jalankan fungsi untuk Inisialisasi awal List
        //presenter.initItems(resources)
        //initItems()


        // Ketika button add diklik, munculkan Dialog untuk mengisi ruangan baru
        btnAdd.setOnClickListener {
            var BuilderDialog = AlertDialog.Builder(this)
            var inflaterDialog = layoutInflater.inflate(R.layout.dialog_add_room,null)
            BuilderDialog.setView(inflaterDialog)
            BuilderDialog.setPositiveButton("ADD"){ dialogInterface: DialogInterface, i: Int ->
                var NamaRoomBaru = inflaterDialog.findViewById<EditText>(R.id.inputNamaRoom)
                var KapasitasBaru = inflaterDialog.findViewById<EditText>(R.id.inputKapasitas)
                // Jalankan fungsi untuk menambah room baru
                addItem(NamaRoomBaru.text.toString(),KapasitasBaru.text.toString().toInt())
                // jalankan fungsi readData untuk mengupdate list
                readData()
                // Jalankan fungsi addRoom di presenter untuk mengatur penambahan ruangan
                //presenter.addRoom(NamaRoomBaru.text.toString(),KapasitasBaru.text.toString())
                //var ImageBaru = inflaterDialog.findViewById<ImageView>(R.id.imageRoom)
                //RoomItems.add(Room(NamaRoomBaru.text.toString(),KapasitasBaru.text.toString().toInt(),imageString))
                //readData()
                //updateAdapter()
            }
            BuilderDialog.create().show()
        }
        // RoomItems yang telah diinisialisasi dimasukkan ke adapter
        RoomListAdminAdapter = RoomListAdminRecyclerViewAdapter(RoomItems,this, interfaceData)
        // Tentukan adapter dan LayoutManager
        recyclerViewRoomList.adapter = RoomListAdminAdapter
        recyclerViewRoomList.layoutManager = LinearLayoutManager(this)
    }

    // Buat fungsi untuk membaca data table room
    fun readData(){
        doAsync {
            //RoomItems = controller.getRoomList()
            RoomItems.clear()
            // Mulai transaction
            roomsqlitedb?.beginRoomTransaction()
            // Baca data ke dalam List yang telah disediakan
            RoomItems = roomsqlitedb?.getRoomTransaction()!!
            // Nyatakan transaction sukses
            roomsqlitedb?.successRoomTransaction()
            // Transaction selesai
            roomsqlitedb?.endRoomTransaction()


            uiThread {
                // Jalankan fungsi updateAdapter() bila selesai melakukan transaction
                updateAdapter()
                Toast.makeText(this@MainActivity,"Refreshed",Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Buat fungsi updateAdapter()
    fun updateAdapter(){
        /*RoomItems.clear()
        doAsync {
            roomsqlitedb?.beginRoomTransaction()
            RoomItems = roomsqlitedb?.getRoomTransaction()!!
            roomsqlitedb?.successRoomTransaction()
            roomsqlitedb?.endRoomTransaction()
        }*/
        //RoomItems = roomsqlitedb?.getRoom()!!

        // Update Adapter untuk RecyclerView
        RoomListAdminAdapter = RoomListAdminRecyclerViewAdapter(RoomItems,this, interfaceData)
        // Tentukan adapter dan LayoutManager
        recyclerViewRoomList.adapter = RoomListAdminAdapter
        recyclerViewRoomList.layoutManager = LinearLayoutManager(this)
    }
    fun getRoomData(){
        RoomItems = roomsqlitedb?.getRoom()!!
        //Log.w("Hasil",RoomItems.toString())
        RoomListAdminAdapter.notifyDataSetChanged()
    }

    // Buat fungsi untuk add room baru
    private fun addItem(title: String, cap: Int){
        /*var lastId = controller.getLastID()
        var newId = lastId + 1
        doAsync {
            controller.insertRoom(Room(newId,title,cap,imageStr))
            uiThread {
                Toast.makeText(this@MainActivity,"Room Added",Toast.LENGTH_SHORT).show()
            }
        }

         */

        //var tmp = Rooms()
        //tmp.title = title
        //tmp.capacity = cap
        //tmp.image = imageStr

        // Jalankan fungsi add room dari db Helper
        roomsqlitedb?.addRoom(Rooms(0,title,cap,""))
    }

    // Fungsi untuk generate string gambar
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
        imageStr = imageString

        //Kembalikan string yang telah di buat
        return imageString
    }

    override fun sendRoomData(id: Int, title: String, capacity: Int, image: String) {
        // Mulai intent untuk update/edit room dengan mengirimkan informasi room berupa id, title, cap dan image
        var intent = Intent(this,UpdateRoomActivity::class.java)
        intent.putExtra(EXTRA_ROOM_ID,id)
        intent.putExtra(EXTRA_ROOM_TITLE,title)
        intent.putExtra(EXTRA_ROOM_CAP,capacity)
        intent.putExtra(EXTRA_ROOM_IMAGE,image)
        //Toast.makeText(this,"${image}",Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        readData()
        //updateAdapter()
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == EXTRA_UPDATE_RESULT){
            Toast.makeText(this,"${data?.getIntExtra(EXTRA_UPDATE_ROOM_ID,0)}",Toast.LENGTH_SHORT).show()
            var id = data?.getIntExtra(EXTRA_UPDATE_ROOM_ID,0)
            var title = data?.getStringExtra(EXTRA_UPDATE_ROOM_TITLE)
            var cap = data?.getIntExtra(EXTRA_UPDATE_ROOM_CAP,0)
            var image = data?.getStringExtra(EXTRA_UPDATE_ROOM_IMAGE)
            doAsync {
                //roomsqlitedb?.beginRoomTransaction()
                roomsqlitedb?.updateRoom(Room(id!!,title!!,cap!!,image!!))
                updateAdapter()
                //roomsqlitedb?.successRoomTransaction()
                //roomsqlitedb?.endRoomTransaction()
            }

        }
    }*/

    /*// Fungsi untuk menampilkan ruangan yang telah diupdate
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
    }*/
}