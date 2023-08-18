package cn.outter.demo.find.api

import com.hjq.http.config.IRequestApi
import java.io.Serializable

class FindUserApi : IRequestApi {
    override fun getApi(): String {
        return "/api/client/user/rec"
    }


    data class FindUserList(var userList: MutableList<FindUser>) : Serializable

    data class FindUser(
        var id: String,
        var name: String?,
        var gender: Int,
        var birthday: String,
        var avatarUrl: String?,
        var photoUrl: String?
    ) : Serializable
}