package cn.outter.demo.database.dao

import androidx.room.*
import cn.outter.demo.database.entity.Message
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
abstract class MessageDao {

    /**
     * 首次查询某个会话下面的聊天记录
     */
    @Transaction
    @Query("select * from outter_messages where session_id = :sessionId order by message_send_time limit 100")
    abstract fun queryAllMessagesBySessionId(sessionId:String): List<Message>?

    /**
     * 下拉加载某个会话某个时间之前的聊天记录
     */
    @Transaction
    @Query("select * from outter_messages where session_id = :sessionId and message_send_time < :sendTime order by message_send_time limit 100")
    abstract fun queryAllMessagesBeforeSometime(sessionId:String, sendTime:Long): List<Message>?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMessage(vararg message: Message):List<Long>?
}