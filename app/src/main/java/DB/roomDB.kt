package DB

import android.provider.BaseColumns

object roomDB {
    class roomTable : BaseColumns{
        companion object{
            val TABLE_ROOM = "tbl_room"
            val COLUMN_ID = "Room_ID"
            val COLUMN_TITLE = "Room_Title"
            val COLUMN_CAP = "Room_Cap"
            val COLUMN_IMAGE = "Room_Image"
        }
    }
}