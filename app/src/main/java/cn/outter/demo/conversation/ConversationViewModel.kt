package cn.outter.demo.conversation

import androidx.lifecycle.MutableLiveData
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.bean.User
import cn.outter.demo.database.ChatDataBaseDelegate
import cn.outter.demo.database.entity.Message
import cn.outter.demo.database.entity.Session
import cn.outter.demo.database.entity.UserInfo
import io.reactivex.MaybeObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class ConversationViewModel : BaseViewModel() {
    val sessionLiveData = MutableLiveData<Session?>()
    val messagesLiveData = MutableLiveData<List<Message>>()

    private val mine = DataCacheInMemory.mine

    fun getSession(toUserId: String) {
        ChatDataBaseDelegate.db.sessions().querySession("${mine?.id}_${toUserId}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MaybeObserver<Session?> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    createSession(toUserId)
                }

                override fun onComplete() {

                }

                override fun onSuccess(t: Session) {
                    sessionLiveData.value = t
                }
            })
    }

    fun createSession(toUserId: String) {
        val session = Session("${mine?.id}_${toUserId}", "", System.currentTimeMillis(), UserInfo("", "", ""))
        ChatDataBaseDelegate.db.sessions().insertSession()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Long>?> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(t: List<Long>) {
                    if (t.isNotEmpty()) {
                        sessionLiveData.value = session
                    } else {
                        sessionLiveData.value = null
                    }
                }

                override fun onError(e: Throwable) {
                    sessionLiveData.value = null
                }
            })
    }

    fun getConversations(sessionId: String) {
        ChatDataBaseDelegate.db.messages().queryAllMessagesBySessionId(sessionId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MaybeObserver<List<Message>?> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    val messages = ArrayList<Message>()
                    messagesLiveData.value = messages
                }

                override fun onComplete() {

                }

                override fun onSuccess(t: List<Message>) {
                    messagesLiveData.value = t
                }

            })
    }
}