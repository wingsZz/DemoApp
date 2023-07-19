package cn.outter.demo.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "outter_sessions")
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "user_info")
    var userInfo: UserInfo
)