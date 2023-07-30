package cn.outter.demo.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson

@Entity(
    tableName = "outter_messages", foreignKeys = [ForeignKey(
        entity = Session::class,
        parentColumns = ["id"],
        childColumns = ["session_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
@TypeConverters(value = [MessageContentConvert::class])
data class Message(
    @ColumnInfo(name = "session_id", index = true)
    val sessionId: String,

    @ColumnInfo(name = "message_content")
    val map: Map<String, Any>?,

    @ColumnInfo(name = "message_type")
    val msgType: Int,

    @ColumnInfo(name = "message_to_id")
    val toId: String,

    @ColumnInfo(name = "message_from_id")
    val fromId: String,

    @ColumnInfo(name = "message_send_time")
    val sendTime:Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @Ignore
    var msgContent:Any? = null
}