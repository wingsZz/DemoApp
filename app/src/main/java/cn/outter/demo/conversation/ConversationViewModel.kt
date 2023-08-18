package cn.outter.demo.conversation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.conversation.api.ChatApi
import cn.outter.demo.database.ChatDataBaseDelegate
import cn.outter.demo.database.entity.ChatUser
import cn.outter.demo.database.entity.Message
import cn.outter.demo.database.entity.Session
import cn.outter.demo.net.HttpData
import cn.outter.demo.session.SessionAction
import com.google.gson.Gson
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.jeremyliao.liveeventbus.LiveEventBus
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

class ConversationViewModel : BaseViewModel() {
    companion object {
        val TAG = "ConversationViewModel"
    }

    val sessionLiveData = MutableLiveData<Session?>()
    val messagesLiveData = MutableLiveData<List<Message>>()
    val remoteMessageLiveData = MutableLiveData<List<Message>>()

    private val mine = DataCacheInMemory.mine
    private val chatApi = ChatApi()

    fun setSession(session: Session) {
        sessionLiveData.value = session
    }

    fun getSession(toUser: ChatUser) {
        Flowable.just("${mine?.id}_${toUser.userId}")
            .subscribeOn(Schedulers.io())
            .map {
                ChatDataBaseDelegate.db.sessions().querySession(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<Session?> {
                override fun onSubscribe(s: Subscription?) {
                    Log.d(TAG, "getSession onSubscribe")
                    s?.request(1)
                }

                override fun onError(t: Throwable?) {
                    Log.d(TAG, "getSession onError ---- ${t?.message}")
                    createSession(toUser)
                }

                override fun onComplete() {
                    Log.d(TAG, "getSession onComplete")
                }

                override fun onNext(t: Session?) {
                    Log.d(TAG, "getSession onNext ---- $t")
                    if (t != null) {
                        sessionLiveData.value = t
                    } else {
                        createSession(toUser)
                    }
                }

            })
    }

    fun createSession(toUser: ChatUser) {
        val session =
            Session("${mine?.id}_${toUser.userId}", "", System.currentTimeMillis(), toUser)
        Flowable.just(session)
            .subscribeOn(Schedulers.io())
            .map {
                val result = ChatDataBaseDelegate.db.sessions().insertSession(session)
                !result.isNullOrEmpty()
            }
            .filter {
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<Boolean> {
                override fun onSubscribe(s: Subscription?) {
                    Log.d(TAG, "createSession onSubscribe")
                    s?.request(1)
                }

                override fun onError(t: Throwable?) {
                    Log.d(TAG, "createSession onError ---- ${t?.message}")
                }

                override fun onComplete() {
                    Log.d(TAG, "createSession onComplete")
                }

                override fun onNext(t: Boolean?) {
                    Log.d(TAG, "createSession onNext ---- $t")
                    if (t == true) {
                        sessionLiveData.value = session
                        LiveEventBus.get<SessionAction>("session_event")
                            .post(SessionAction(0, session))
                    } else {
                        sessionLiveData.value = null
                    }
                }

            })
    }

    fun getConversations(lastSendTime: Long) {
        if (sessionLiveData.value == null) {
            return
        }
        Flowable.just(lastSendTime)
            .subscribeOn(Schedulers.io())
            .map {
                val result = ChatDataBaseDelegate.db.messages()
                    .queryAllMessagesBeforeSometime(sessionLiveData.value!!.id, lastSendTime)
                result
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<List<Message>?> {
                override fun onSubscribe(s: Subscription?) {
                    Log.d(TAG, "getConversations onSubscribe")
                    s?.request(1)
                }

                override fun onError(t: Throwable?) {
                    Log.d(TAG, "getConversations onError ---- ${t?.message}")
                    val messages = ArrayList<Message>()
                    messagesLiveData.value = messages
                }

                override fun onComplete() {
                    Log.d(TAG, "getConversations onComplete")
                }

                override fun onNext(t: List<Message>?) {
                    Log.d(TAG, "getConversations onNext")
                    messagesLiveData.value = t
                }

            })
    }

    fun sendTxtMessage(text: String, message: Message, lifecycleOwner: LifecycleOwner) {
        val txtMsgMap = mapOf(Pair("text", text))
        val chatRequest = ChatApi.ChatRequest(message.toId, "TEXT", txtMsgMap)
        insertMessage(message)
        EasyHttp.post(lifecycleOwner)
            .api(chatApi)
            .json(Gson().toJson(chatRequest))
            .request(object : OnHttpListener<HttpData<ChatApi.ChatResponse?>> {
                override fun onHttpSuccess(result: HttpData<ChatApi.ChatResponse?>?) {
                    if (result?.data != null && result.data.replyMsgList.isNotEmpty()) {
                        val messageList = ArrayList<Message>()
                        result.data.replyMsgList.forEach {
                            if (it.msgType == "TEXT") {
                                val txtMsg = MsgFactory.createTxtMsg(it.msgDetail["text"] as String,mine?.id?:"",it.from,false)
                                messageList.add(txtMsg)
                            } else {
                                val imgMsg = MsgFactory.createPicMsg("",it.msgDetail["imageUrl"] as String,0,0,mine?.id?:"",it.from,false)
                                messageList.add(imgMsg)
                            }
                        }
                        insertMessage(*messageList.toTypedArray())
                        remoteMessageLiveData.value = messageList
                    }
                }

                override fun onHttpFail(e: Exception?) {

                }

            })
    }

    fun sendPicMessage(imagePath: String, message: Message, lifecycleOwner: LifecycleOwner) {
        val picMsgMap = mapOf(Pair("imageUrl", imagePath))
        val chatRequest = ChatApi.ChatRequest(message.toId, "PIC", picMsgMap)
        insertMessage(message)
        EasyHttp.post(lifecycleOwner)
            .api(chatApi)
            .json(Gson().toJson(chatRequest))
            .request(object : OnHttpListener<HttpData<ChatApi.ChatResponse?>> {
                override fun onHttpSuccess(result: HttpData<ChatApi.ChatResponse?>?) {
                    if (result?.data != null && result.data.replyMsgList.isNotEmpty()) {
                        val messageList = ArrayList<Message>()
                        result.data.replyMsgList.forEach {
                            if (it.msgType == "TEXT") {
                                val txtMsg = MsgFactory.createTxtMsg(it.msgDetail["text"] as String,mine?.id?:"",it.from,false)
                                messageList.add(txtMsg)
                            } else {
                                val imgMsg = MsgFactory.createPicMsg("",it.msgDetail["imageUrl"] as String,0,0,mine?.id?:"",it.from,false)
                                messageList.add(imgMsg)
                            }
                        }
                        insertMessage(*messageList.toTypedArray())
                        messagesLiveData.value = messageList
                    }
                }

                override fun onHttpFail(e: Exception?) {

                }

            })
    }

    private fun insertMessage(vararg message: Message) {
        Flowable.just(message)
            .subscribeOn(Schedulers.io())
            .map {
                val result = ChatDataBaseDelegate.db.messages().insertMessage(*message)
                !result.isNullOrEmpty()
            }
            .filter {
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<Boolean> {
                override fun onSubscribe(s: Subscription?) {
                    Log.d(TAG, "insertMessage onSubscribe")
                    s?.request(1)
                }

                override fun onError(t: Throwable?) {
                    Log.d(TAG, "insertMessage onError ---- ${t?.message}")
                }

                override fun onComplete() {
                    Log.d(TAG, "insertMessage onComplete")
                }

                override fun onNext(t: Boolean?) {
                    Log.d(TAG, "insertMessage onNext ---- $t")
                }

            })
    }
}