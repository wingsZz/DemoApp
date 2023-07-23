package cn.outter.demo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.outter.demo.database.ChatDataBaseDelegate
import cn.outter.demo.database.entity.Session
import io.reactivex.Flowable
import io.reactivex.MaybeObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import shark.AndroidServices

class ChatViewModel : BaseViewModel() {
    val sessions = MutableLiveData<List<Session>?>()

    fun getAllSession() {
        ChatDataBaseDelegate.db.sessions().queryAllSessions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MaybeObserver<List<Session>?> {
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
                    sessions.value = t
                }

            })
    }
}