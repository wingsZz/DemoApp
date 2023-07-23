package cn.outter.demo.database.dao

import androidx.room.*
import cn.outter.demo.database.entity.Message
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
abstract class MessageDao {

    @Transaction
    @Query("select * from outter_messages where session_id = :sessionId")
    abstract fun queryAllMessagesBySessionId(sessionId:Long): Maybe<List<Message>?>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMessage(vararg message: Message):Single<List<Long>>
}