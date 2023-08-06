package cn.outter.demo.conversation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.DatabaseMockUtil
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.bean.User
import cn.outter.demo.conversation.api.ChatApi
import cn.outter.demo.database.ChatDataBaseDelegate
import cn.outter.demo.database.entity.Message
import cn.outter.demo.database.entity.Session
import cn.outter.demo.database.entity.UserInfo
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
        ChatDataBaseDelegate.db.sessions().querySession("1")
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
                    Log.d(TAG, "query session result = $t")
                    sessionLiveData.value = t
                }
            })
    }

    fun createSession(toUserId: String) {
        // ${mine?.id}_${toUserId}
        val session = Session("1", "", System.currentTimeMillis(), UserInfo("", "", ""))
        ChatDataBaseDelegate.db.sessions().insertSession()
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
        val txtMsgMap = mapOf(Pair("text",text))
        val chatRequest = ChatApi.ChatRequest(message.toId,"TEXT",txtMsgMap)
        ChatDataBaseDelegate.db.messages().insertMessage(message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("DatabaseDebug","haha -> ${it.size}")
            }
            ) { t ->
                Log.d("DatabaseDebug","haha -> ${t.message}")
            }
        EasyHttp.post(lifecycleOwner)
            .api(chatApi)
            .json(Gson().toJson(chatRequest))
            .request(object :OnHttpListener<HttpData<ChatApi.ChatResponse?>> {
                override fun onHttpSuccess(result: HttpData<ChatApi.ChatResponse?>?) {

                }

                override fun onHttpFail(e: Exception?) {

                }

            })
    }

    fun sendPicMessage(imagePath: String, toUserId: String, lifecycleOwner: LifecycleOwner) {
        val picMsgMap = mapOf(Pair("imageUrl",imagePath))
        val chatRequest = ChatApi.ChatRequest(toUserId,"PIC",picMsgMap)
        EasyHttp.post(lifecycleOwner)
            .api(chatApi)
            .json(Gson().toJson(chatRequest))
            .request(object :OnHttpListener<HttpData<ChatApi.ChatResponse?>> {
                override fun onHttpSuccess(result: HttpData<ChatApi.ChatResponse?>?) {

                }

                override fun onHttpFail(e: Exception?) {

                }

            })
    }
}