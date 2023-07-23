package cn.outter.demo.database.dao

import androidx.room.*
import cn.outter.demo.database.entity.Message
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
abstract class MessageDao {

    @Query("select * from outter_messages where session_id = :sessionId")
    abstract fun queryAllMessagesBySessionId(sessionId:Long): Flowable<List<Message>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMessage(vararg message: Message):Single<List<Long>>
}