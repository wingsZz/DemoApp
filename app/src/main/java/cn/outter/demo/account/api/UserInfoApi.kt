package cn.outter.demo.account.api

import com.hjq.http.config.IRequestApi
import java.io.Serializable

class UserInfoApi: IRequestApi {
    override fun getApi(): String {
        return "/api/client/user/info/get"
    }

    data class UserInfo(
        var username:String,
        var nickname:String,
        var gender:Int,
        var birthday:String,
        var avatarUrl:String,
        var photoUrl:String
    ):Serializable
}