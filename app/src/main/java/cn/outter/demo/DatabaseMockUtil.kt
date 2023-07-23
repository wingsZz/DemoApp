package cn.outter.demo

import android.util.Log
import cn.outter.demo.database.ChatDataBaseDelegate
import cn.outter.demo.database.entity.Session
import cn.outter.demo.database.entity.UserInfo
import io.reactivex.FlowableSubscriber
import io.reactivex.MaybeObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscription
import timber.log.Timber

object DatabaseMockUtil {
    fun mockSession() {
        val sessions = ArrayList<Session>(100)
        for (i in 0 until 100) {
            val userInfo = UserInfo(i.toString(), "name_$i", "url_$i")
            val session = Session(i.toLong(), userInfo)
            sessions.add(session)
        }


        ChatDataBaseDelegate.db.sessions().insertSession(*sessions.toTypedArray())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("DatabaseDebug","haha -> ${it.size}")
                getAllSession()
            }
            ) { t ->
                t?.message
            }
    }

    fun getAllSession() {
        ChatDataBaseDelegate.db.sessions().queryAllSessions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :MaybeObserver<List<Session>?> {
                override fun onSubscribe(d: Disposable) {
                    Log.d("DatabaseDebug","haha -> --")
                }

                override fun onError(e: Throwable) {
                    Log.d("DatabaseDebug","haha -> ---")
                }

                override fun onComplete() {
                    Log.d("DatabaseDebug","haha -> ----")
                }

                override fun onSuccess(t: List<Session>) {
                    Log.d("DatabaseDebug","haha -> ------")
                }

            })
    }
}