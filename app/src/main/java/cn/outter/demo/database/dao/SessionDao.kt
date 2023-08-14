package cn.outter.demo.database.dao

import androidx.room.*
import cn.outter.demo.database.entity.Session
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
abstract class SessionDao {

    @Transaction
    @Query("select * from outter_sessions order by last_message_time DESC")
    abstract fun queryAllSessions():List<Session>?

    @Transaction
    @Query("select * from outter_sessions where id = :sessionId")
    abstract fun querySession(sessionId:String):Session?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSession(vararg session: Session):List<Long>?

    @Transaction
    @Update
    abstract fun updateSession(vararg session: Session):Int
}