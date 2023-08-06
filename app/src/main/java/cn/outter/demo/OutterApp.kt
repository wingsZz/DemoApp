package cn.outter.demo

import android.app.Application
import cn.outter.demo.net.ReleaseServer
import cn.outter.demo.net.RequestHandler
import cn.outter.demo.net.TestServer
import com.hjq.http.EasyConfig
import com.hjq.http.config.IRequestInterceptor
import com.hjq.http.config.IRequestServer
import com.hjq.http.model.HttpHeaders
import com.hjq.http.model.HttpParams
import com.hjq.http.request.HttpRequest
import com.hjq.toast.ToastUtils
import com.tencent.mmkv.MMKV
import okhttp3.OkHttpClient


class OutterApp : Application() {

    companion object {
        @JvmStatic
        lateinit var application: Application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        MMKV.initialize(this)

        ToastUtils.init(this)

        initNet()
    }

    private fun initNet() {
        val server: IRequestServer
        if (BuildConfig.DEBUG) {
            server = TestServer()
        } else {
            server = ReleaseServer()
        }
        val okHttpClient = OkHttpClient.Builder()
            .build()
        EasyConfig.with(okHttpClient) // 是否打印日志
            .setLogEnabled(BuildConfig.DEBUG)
            // 设置服务器配置（必须设置）
            .setServer(server) // 设置请求处理策略（必须设置）
            .setHandler(RequestHandler(this)) // 设置请求参数拦截器
            .setInterceptor(object : IRequestInterceptor {
                override fun interceptArguments(
                    httpRequest: HttpRequest<*>,
                    params: HttpParams,
                    headers: HttpHeaders
                ) {
                    headers.put(
                        "timestamp",
                        System.currentTimeMillis().toString()
                    )
                }

            }) // 设置请求重试次数
            .setRetryCount(1) // 设置请求重试时间
            .setRetryTime(2000) // 添加全局请求参数
            .into()
    }
}