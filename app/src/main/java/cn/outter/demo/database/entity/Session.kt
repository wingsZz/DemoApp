package cn.outter.demo.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "outter_sessions")
@TypeConverters(value = [UserInfoConvert::class])
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "user_info")
    var userInfo: UserInfo
)