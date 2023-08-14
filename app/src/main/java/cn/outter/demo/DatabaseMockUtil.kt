package cn.outter.demo

import android.util.Log
import cn.outter.demo.database.ChatDataBaseDelegate
import cn.outter.demo.database.entity.ImgMsg
import cn.outter.demo.database.entity.Message
import cn.outter.demo.database.entity.MsgType
import cn.outter.demo.database.entity.Session
import cn.outter.demo.database.entity.TxtMsg
import cn.outter.demo.database.entity.ChatUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object DatabaseMockUtil {
    fun mockSession() {
        val sessions = ArrayList<Session>(100)
        for (i in 0 until 100) {
            val chatUser = ChatUser(i.toString(), "name_$i", "url_$i")
            val session = Session(i.toString(),"",System.currentTimeMillis(), chatUser)
            sessions.add(session)
        }


        ChatDataBaseDelegate.db.sessions().insertSession(*sessions.toTypedArray())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("DatabaseDebug","haha -> ${it?.size}")
                getAllSession()
            }
            ) { t ->
                t?.message
            }
    }

    fun mockMessage() {
        val messages = ArrayList<Message>(1000)
        for (i in 0 until 1000) {
            val msgType = java.util.Random().nextInt(2) + 1
            if (msgType == MsgType.TXT) {
                val msg = TxtMsg()
                msg.msgType = MsgType.TXT
                msg.content = "哈哈哈哈$i"
                val map = HashMap<String,Any>()
                map.put("content",msg.toString())
                val message = Message("1",map,MsgType.TXT,"1","1",System.currentTimeMillis())
                messages.add(message)
            } else {
                val msg = ImgMsg()
                msg.msgType = MsgType.PIC
                msg.imageH = i
                msg.imageW = i
                msg.imageUrl = "url_$i"
                msg.imageLocalPath = "local_$i"
                val map = HashMap<String,Any>()
                map.put("content",msg.toString())
                val message = Message("1",map,MsgType.PIC,"1","1",System.currentTimeMillis())
                messages.add(message)
            }
        }

        ChatDataBaseDelegate.db.messages().insertMessage(*messages.toTypedArray())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("DatabaseDebug","haha -> ${it.size}")
                getAllSession()
            }
            ) { t ->
                Log.d("DatabaseDebug","haha -> ${t.message}")
            }
    }

    fun getAllSession() {
//        ChatDataBaseDelegate.db.sessions().queryAllSessions()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object :MaybeObserver<List<Session>?> {
//                override fun onSubscribe(d: Disposable) {
//                    Log.d("DatabaseDebug","haha -> --")
//                }
//
//                override fun onError(e: Throwable) {
//                    Log.d("DatabaseDebug","haha -> ---")
//                }
//
//                override fun onComplete() {
//                    Log.d("DatabaseDebug","haha -> ----")
//                }
//
//                override fun onSuccess(t: List<Session>) {
//                    Log.d("DatabaseDebug","haha -> ------")
//                }
//
//            })
    }
}