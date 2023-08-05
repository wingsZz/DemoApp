package cn.outter.demo.account.api

import com.hjq.http.config.IRequestApi

class LoginApi : IRequestApi {
    private var userName: String = ""
    private var password: String = ""

    override fun getApi(): String {
        return "/api/client/login"
    }

    fun setUSerName(userName: String): LoginApi {
        this.userName = userName
        return this
    }

    fun setPassword(password: String): LoginApi {
        this.password = password
        return this
    }
}