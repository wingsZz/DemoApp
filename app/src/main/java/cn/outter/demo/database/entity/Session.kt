package cn.outter.demo.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "outter_sessions")
@TypeConverters(value = [UserInfoConvert::class])
data class Session(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "last_message_content")
    var lastMessageContent:String,

    @ColumnInfo(name = "last_message_time")
    var lastMessageTime:Long,

    @ColumnInfo(name = "user_info")
    var userInfo: UserInfo
):java.io.Serializable {
    override fun toString(): String {
        return "Session(id='$id', lastMessageContent='$lastMessageContent', lastMessageTime=$lastMessageTime, userInfo=$userInfo)"
    }
}