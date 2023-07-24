package cn.outter.demo.net.exception

import cn.outter.demo.net.HttpData
import com.hjq.http.exception.HttpException

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/EasyHttp
 * time   : 2019/06/25
 * desc   : 返回结果异常
 */
class ResultException : HttpException {
    val httpData: HttpData<*>

    constructor(message: String?, data: HttpData<*>) : super(message) {
        httpData = data
    }

    constructor(message: String?, cause: Throwable?, data: HttpData<*>) : super(message, cause) {
        httpData = data
    }
}