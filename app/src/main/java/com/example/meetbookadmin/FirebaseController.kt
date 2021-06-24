package com.example.meetbookadmin

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseController (context: Context) {
    private var database = Firebase.database
    private val ref = FirebaseDatabase.getInstance().getReference().child("ROOMS")
    private val mContext = context
    private var roomData = mutableMapOf<String,String>()
    private var roomList : MutableList<Room> = mutableListOf()

    fun insertRoom(room: Room){
        var roomID = ref.push().key.toString()

        ref.child(roomID).setValue(room).apply{
            addOnCompleteListener {
                Toast.makeText(mContext, "Room Created", Toast.LENGTH_SHORT)
            }
            addOnCanceledListener {  }
            addOnFailureListener {
                Toast.makeText(mContext, "Room Gagal Create", Toast.LENGTH_SHORT)
            }
            addOnSuccessListener {  }
        }
    }

    private fun readRoom() : MutableMap<String,String>{
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()){
                    roomData.clear()
                    for(data in snapshot.children){
                        val room = data.getValue(Room::class.java)
                        room.let{
                            roomData.put(data.key.toString(),it!!.title)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return roomData
    }

    fun getRoomList() : MutableList<Room>{
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()){
                    roomList.clear()
                    for(data in snapshot.children){
                        val room = data.getValue(Room::class.java)
                        room.let{
                            roomList.add(Room(it!!.id,it!!.title,it!!.capacity,it!!.image))
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return roomList
    }

    fun validityRoom(srcTitle : String) : Boolean{
        val getList = getRoomList()
        var state = true
        for (data in getList){
            if (data.title == srcTitle){
                state = false
                break
            }
        }
        return state
    }

    fun getLastID() : Int{
        var lastId = 0
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()){
                    for(data in snapshot.children){
                        val room = data.getValue(Room::class.java)
                        room.let{
                            lastId = it!!.id
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return lastId
    }

    fun updateRoom(oldTitle: String, newId: Int, newTitle: String, newCap: Int, newImage: String){
        val key = readRoom().filterValues { it == oldTitle }.keys
        val roomID = key.first()
        val updatedData = Room(newId,newTitle,newCap,newImage)
        ref.child(roomID).setValue(updatedData).apply{
            addOnCompleteListener{
                Toast.makeText(mContext, "Room Updated", Toast.LENGTH_SHORT)
            }
            addOnFailureListener {
                Toast.makeText(mContext, "Room Update Failed", Toast.LENGTH_SHORT)
            }
        }
    }
}