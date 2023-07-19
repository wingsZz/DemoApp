package cn.outter.demo.database.dao

import androidx.room.Dao
import androidx.room.Query
import cn.outter.demo.database.entity.Message
import cn.outter.demo.database.entity.Session
import io.reactivex.Flowable

@Dao
abstract class SessionDao {

    @Query("select * from outter_sessions")
    abstract fun queryAllSessions():Flowable<List<Session>>
}