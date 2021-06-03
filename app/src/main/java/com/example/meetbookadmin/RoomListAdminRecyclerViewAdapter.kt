package com.example.meetbookadmin

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Class Adapter untuk adapter Room List
class RoomListAdminRecyclerViewAdapter (data : MutableList<Rooms>, context: Context, interfaceData: InterfaceData) : RecyclerView.Adapter<RoomListAdminRecyclerViewAdapter.Holder>(){
    private var RoomData = data
    private var Ctxt = context
    private var interfaceData: InterfaceData = interfaceData
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val roomtitle = itemView.findViewById<TextView>(R.id.MeetRoomTitle)
        val roomcap = itemView.findViewById<TextView>(R.id.MeetRoomCap)
        val roomimage = itemView.findViewById<ImageView>(R.id.MeetRoomImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.room_list, parent, false)

        return Holder(inflate)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.roomtitle.setText(RoomData.get(position).title)

        val returnedCap = RoomData.get(position).capacity
        holder.roomcap.setText("$returnedCap Seats")

        val imageBytes = Base64.decode(RoomData.get(position).image, Base64.DEFAULT)
        val decodeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        holder.roomimage.setImageBitmap(decodeImage)

        holder.itemView.setOnClickListener {
            val model = RoomData.get(position)
            interfaceData.sendRoomData(model.id,model.title,model.capacity,model.image)
        }
    }

    override fun getItemCount(): Int {
        return RoomData.size
    }
}