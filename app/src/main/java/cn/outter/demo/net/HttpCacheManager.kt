package cn.outter.demo.net

import kotlin.jvm.Volatile
import com.tencent.mmkv.MMKV
import cn.outter.demo.net.HttpCacheManager
import com.hjq.http.config.IRequestApi
import com.hjq.gson.factory.GsonFactory
import com.hjq.http.request.HttpRequest

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/EasyHttp
 * time   : 2022/03/22
 * desc   : Http 缓存管理器
 */
object HttpCacheManager {
    @Volatile
    private var sMmkv: MMKV? = null

    /**
     * 获取单例的 MMKV 实例
     */
    @JvmStatic
    val mmkv: MMKV?
        get() {
            if (sMmkv == null) {
                synchronized(RequestHandler::class.java) {
                    if (sMmkv == null) {
                        sMmkv = MMKV.mmkvWithID("http_cache_id")
                    }
                }
            }
            return sMmkv
        }

    /**
     * 生成缓存的 key
     */
    @JvmStatic
    fun generateCacheKey(httpRequest: HttpRequest<*>): String {
        val requestApi = httpRequest.requestApi
        return """
             用户 id
             ${requestApi.api}
             ${GsonFactory.getSingletonGson().toJson(requestApi)}
             """.trimIndent()
    }
}