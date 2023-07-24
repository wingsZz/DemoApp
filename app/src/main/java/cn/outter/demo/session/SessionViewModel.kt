package cn.outter.demo.session

import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.database.ChatDataBaseDelegate
import cn.outter.demo.database.entity.Session
import io.reactivex.Flowable
import io.reactivex.MaybeObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SessionViewModel : BaseViewModel() {
    companion object {
        val TAG = "SessionViewModel"
    }

    val sessions = MutableLiveData<List<Session>?>()

    fun getAllSession() {
        ChatDataBaseDelegate.db.sessions().queryAllSessions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MaybeObserver<List<Session>?> {
                override fun onSubscribe(d: Disposable) {
                    Log.d(TAG, "getAllSession onSubscribe ---- ${Thread.currentThread().name}")
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "getAllSession onError ---- ${e.message}")
                    sessions.value = null
                }

                override fun onComplete() {
                    Log.d(TAG, "getAllSession onComplete ---- ")
                }

                override fun onSuccess(t: List<Session>) {
                    Log.d(TAG, "getAllSession onSuccess ---- ")
                    sessions.value = t
                }

            })
    }

    fun updateSessionLastMessageTime(session: Session) {
        session.lastMessageTime = System.currentTimeMillis()
        ChatDataBaseDelegate.db.sessions().insertSession(session)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Long>?> {
                override fun onSubscribe(d: Disposable) {
                    Log.d(TAG, "updateSessionLastMessageTime onSubscribe ---- ${Thread.currentThread().name}")
                }

                override fun onSuccess(t: List<Long>) {
                    Log.d(TAG, "updateSessionLastMessageTime onSuccess ---- ${t[0]}")
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "updateSessionLastMessageTime onError ---- ${e.message}")
                }
            })
    }
}