package cn.outter.demo.database.dao

import androidx.room.*
import cn.outter.demo.database.entity.Session
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
abstract class SessionDao {

    @Transaction
    @Query("select * from outter_sessions")
    abstract fun queryAllSessions():Maybe<List<Session>?>

    @Transaction
    @Query("select * from outter_sessions where id = :sessionId")
    abstract fun querySession(sessionId:Long):Maybe<Session?>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSession(vararg session: Session):Single<List<Long>>
}