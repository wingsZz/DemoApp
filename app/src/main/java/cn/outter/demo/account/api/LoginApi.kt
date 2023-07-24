package cn.outter.demo.account.api

import com.hjq.http.config.IRequestApi

class LoginApi : IRequestApi {
    private var account: String = ""
    private var password: String = ""

    override fun getApi(): String {
        return ""
    }

    fun setAccount(account: String): LoginApi {
        this.account = account
        return this
    }

    fun setPassword(password: String): LoginApi {
        this.password = password
        return this
    }
}