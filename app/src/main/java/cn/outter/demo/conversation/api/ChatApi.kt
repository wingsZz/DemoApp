package cn.outter.demo.conversation.api

import com.hjq.http.config.IRequestApi
import java.io.Serializable

class ChatApi : IRequestApi {
    override fun getApi(): String {
        return "/api/client/chat"
    }

    data class ChatRequest(
        var to: String,
        var msgType: String,
        var msgDetail: Map<String, Any>
    ):Serializable

    data class ChatResponse(var replyMsgList: MutableList<ReplayMsg>) : Serializable

    data class ReplayMsg(
        var msgType: String,
        var msgDetail: Map<String, Any>,
        var from: String,
        var to: String,
        var timestamp: Long
    ) : Serializable

    data class TxtMsg(var text: String) : Serializable

    data class PicMsg(var imageUrl: String) : Serializable
}