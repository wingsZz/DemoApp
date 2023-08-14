package cn.outter.demo.session

import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.database.ChatDataBaseDelegate
import cn.outter.demo.database.entity.Session
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

class SessionViewModel : BaseViewModel() {
    companion object {
        val TAG = "SessionViewModel"
    }

    val sessions = MutableLiveData<List<Session>?>()

    fun getAllSession() {
        Flowable.just("")
            .subscribeOn(Schedulers.io())
            .map {
                ChatDataBaseDelegate.db.sessions().queryAllSessions()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<List<Session>?> {
                override fun onSubscribe(s: Subscription?) {
                    Log.d(TAG, "getAllSession onSubscribe")
                    s?.request(1)
                }

                override fun onError(t: Throwable?) {
                    Log.d(TAG, "getAllSession onError ---- ${t?.message}")
                }

                override fun onComplete() {
                    Log.d(TAG, "getAllSession onComplete")
                }

                override fun onNext(t: List<Session>?) {
                    Log.d(TAG, "getAllSession onNext ---- $t")
                    sessions.value = t
                }

            })
    }

    fun updateSessionLastMessageTime(session: Session) {
        session.lastMessageTime = System.currentTimeMillis()
        Flowable.just(session)
            .subscribeOn(Schedulers.io())
            .map {
                val result = ChatDataBaseDelegate.db.sessions().updateSession(session)
                result > 0
            }
            .filter {
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<Boolean> {
                override fun onSubscribe(s: Subscription?) {
                    Log.d(TAG, "updateSessionLastMessageTime onSubscribe")
                    s?.request(1)
                }

                override fun onError(t: Throwable?) {
                    Log.d(TAG, "updateSessionLastMessageTime onError ---- ${t?.message}")

                }

                override fun onComplete() {
                    Log.d(TAG, "updateSessionLastMessageTime onComplete")
                }

                override fun onNext(t: Boolean?) {
                    Log.d(TAG, "updateSessionLastMessageTime onNext ---- $t")
                }

            })
    }
}