package cn.outter.demo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.outter.demo.database.dao.MessageDao
import cn.outter.demo.database.dao.SessionDao
import cn.outter.demo.database.entity.Message
import cn.outter.demo.database.entity.Session

@Database(entities = [Session::class, Message::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun sessions(): SessionDao

    abstract fun messages(): MessageDao
}