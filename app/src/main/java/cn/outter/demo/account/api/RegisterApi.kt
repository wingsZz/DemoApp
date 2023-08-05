package cn.outter.demo.account.api

import com.hjq.http.config.IRequestApi
import java.io.Serializable

class RegisterApi : IRequestApi {
    private var userName: String = ""
    private var password: String = ""

    override fun getApi(): String {
        return "/api/client/register"
    }

    fun setUSerName(userName: String): RegisterApi {
        this.userName = userName
        return this
    }

    fun setPassword(password: String): RegisterApi {
        this.password = password
        return this
    }

    data class RegisterRequest(
        var userName:String = "",
        var password:String = ""
    ):Serializable {
        
    }
}