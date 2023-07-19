package cn.outter.demo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import cn.outter.demo.database.entity.Message
import io.reactivex.Flowable

@Dao
abstract class MessageDao {

    @Query("select * from outter_messages where session_id = :sessionId")
    abstract fun queryAllMessagesBySessionId(sessionId:Long): Flowable<List<Message>>

    @Transaction
    @Insert
    abstract fun insertMessage(message: Message)
}