package cn.outter.demo.net

import com.hjq.http.config.IRequestServer

class ReleaseServer:IRequestServer {
    override fun getHost(): String {
        return "https://chat.chengbro.com"
    }
}