package cn.outter.demo.net

import com.hjq.http.config.IRequestServer

class TestServer:IRequestServer {
    override fun getHost(): String {
        return ""
    }
}