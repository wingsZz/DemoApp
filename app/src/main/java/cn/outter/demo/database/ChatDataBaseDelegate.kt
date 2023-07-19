package cn.outter.demo.database

import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import cn.outter.demo.OutterApp
import cn.outter.demo.database.entity.UserInfoConvert

object ChatDataBaseDelegate {
    //数据库名
    private const val dbName: String = "outter_chat"

    private val convert: UserInfoConvert by lazy {
        UserInfoConvert()
    }

    val db: ChatDatabase by lazy {
        Room.databaseBuilder(
            OutterApp.application, ChatDatabase::class.java, dbName
        ).allowMainThreadQueries()//允许在主线程操作
            .addTypeConverter(convert)
            .addCallback(DbCreateCallBack)//增加回调监听
            .addMigrations()//增加数据库迁移
            .build()
    }

    private object DbCreateCallBack : RoomDatabase.Callback() {
        //第一次创建数据库时调用
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.e("TAG", "first onCreate db version: " + db.version)
        }
    }
}