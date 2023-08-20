package cn.outter.demo.user.api

import com.hjq.http.config.IRequestApi

class ModifyUserInfoApi:IRequestApi {
    override fun getApi(): String {
        return "/api/client/user/info/save"
    }
}