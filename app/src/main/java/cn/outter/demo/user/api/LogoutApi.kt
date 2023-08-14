package cn.outter.demo.user.api

import com.hjq.http.config.IRequestApi

class LogoutApi:IRequestApi {
    override fun getApi(): String {
        return "/api/client/logout"
    }
}