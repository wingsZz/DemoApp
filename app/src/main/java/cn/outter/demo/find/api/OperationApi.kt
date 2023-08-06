package cn.outter.demo.find.api

import com.hjq.http.config.IRequestApi
import java.io.Serializable

class OperationApi : IRequestApi {
    override fun getApi(): String {
        return "/api/client/user/like"
    }

    data class OperationRequest(
        var botId: String,
        var likeType: Int
    ):Serializable
}