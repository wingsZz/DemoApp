package cn.outter.demo.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "outter_messages")
@ForeignKey(entity = Session::class, parentColumns = ["id"],childColumns = ["session_id"], onDelete = ForeignKey.CASCADE )
data class Message(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "session_id") val sessionId: Long,

    @ColumnInfo(name = "last_name") val lastName: String?
)