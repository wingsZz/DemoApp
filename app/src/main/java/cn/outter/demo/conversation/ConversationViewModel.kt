package cn.outter.demo.conversation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.conversation.api.ChatApi
import cn.outter.demo.database.ChatDataBaseDelegate
import cn.outter.demo.database.entity.Message
import cn.outter.demo.database.entity.Session
import cn.outter.demo.database.entity.ChatUser
import cn.outter.demo.net.HttpData
import com.google.gson.Gson
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import io.reactivex.MaybeObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class ConversationViewModel : BaseViewModel() {
    companion object {
        val TAG = "ConversationViewModel"
    }

    val sessionLiveData = MutableLiveData<Session?>()
    val messagesLiveData = MutableLiveData<List<Message>>()

    private val mine = DataCacheInMemory.mine
    private val chatApi = ChatApi()

    fun getSession(toUserId: String) {
        // ${mine?.id}_${toUserId}
        //object : MaybeObserver<Session?> {
        //                override fun onSubscribe(d: Disposable) {
        //                    Log.d(TAG, "onSubscribe")
        //                }
        //
        //                override fun onError(e: Throwable) {
        //                    createSession(toUserId)
        //                }
        //
        //                override fun onComplete() {
        //                    Log.d(TAG, "onComplete")
        //                }
        //
        //                override fun onSuccess(t: Session) {
        //                    Log.d(TAG, "query session result = $t")
        //                    sessionLiveData.value = t
        //                }
        //            }
        ChatDataBaseDelegate.db.sessions().querySession("${mine?.id}_${toUserId}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Session?> {
                override fun onSubscribe(d: Disposable) {
                    Log.d(TAG, "onSubscribe")
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "onError")
                    createSession(toUserId)
                }

                override fun onSuccess(t: Session) {
                    Log.d(TAG, "query session result = $t")
                    sessionLiveData.value = t
                }
            })
    }

    fun createSession(toUserId: String) {
        val session =
            Session("${mine?.id}_${toUserId}", "", System.currentTimeMillis(), ChatUser("", "", ""))
        ChatDataBaseDelegate.db.sessions().insertSession(session)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Long>?> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(t: List<Long>) {
                    if (t.isNotEmpty()) {
                        Log.d(TAG, "create session result = ${t.size}")
                        sessionLiveData.value = session
                    } else {
                        Log.d(TAG, "create session failed!")
                        sessionLiveData.value = null
                    }
                }

                override fun onError(e: Throwable) {
                    sessionLiveData.value = null
                }
            })
    }

    fun getConversations(sessionId: String, lastSendTime: Long) {
        ChatDataBaseDelegate.db.messages().queryAllMessagesAfterSometime("1", lastSendTime)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MaybeObserver<List<Message>?> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "query message failed")
                    val messages = ArrayList<Message>()
                    messagesLiveData.value = messages
                }

                override fun onComplete() {

                }

                override fun onSuccess(t: List<Message>) {
                    Log.d(TAG, "query message result = ${t.size}")
                    messagesLiveData.value = t
                }

            })
    }

    fun sendTxtMessage(text: String, message: Message, lifecycleOwner: LifecycleOwner) {
        val txtMsgMap = mapOf(Pair("text", text))
        val chatRequest = ChatApi.ChatRequest(message.toId, "TEXT", txtMsgMap)
        ChatDataBaseDelegate.db.messages().insertMessage(message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("DatabaseDebug", "haha -> ${it.size}")
            }
            ) { t ->
                Log.d("DatabaseDebug", "haha -> ${t.message}")
            }
        EasyHttp.post(lifecycleOwner)
            .api(chatApi)
            .json(Gson().toJson(chatRequest))
            .request(object : OnHttpListener<HttpData<ChatApi.ChatResponse?>> {
                override fun onHttpSuccess(result: HttpData<ChatApi.ChatResponse?>?) {

                }

                override fun onHttpFail(e: Exception?) {

                }

            })
    }

    fun sendPicMessage(imagePath: String, message: Message, lifecycleOwner: LifecycleOwner) {
        val picMsgMap = mapOf(Pair("imageUrl", imagePath))
        val chatRequest = ChatApi.ChatRequest(message.toId, "PIC", picMsgMap)
        ChatDataBaseDelegate.db.messages().insertMessage(message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("DatabaseDebug", "haha -> ${it.size}")
            }
            ) { t ->
                Log.d("DatabaseDebug", "haha -> ${t.message}")
            }
        EasyHttp.post(lifecycleOwner)
            .api(chatApi)
            .json(Gson().toJson(chatRequest))
            .request(object : OnHttpListener<HttpData<ChatApi.ChatResponse?>> {
                override fun onHttpSuccess(result: HttpData<ChatApi.ChatResponse?>?) {

                }

                override fun onHttpFail(e: Exception?) {

                }

            })
    }
}