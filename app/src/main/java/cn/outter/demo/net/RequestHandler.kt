package cn.outter.demo.net

import android.app.Application
import android.content.Context
import cn.outter.demo.net.HttpCacheManager.generateCacheKey
import cn.outter.demo.net.HttpCacheManager.mmkv
import com.hjq.http.config.IRequestHandler
import kotlin.Throws
import cn.outter.demo.R
import okhttp3.ResponseBody
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.hjq.http.EasyLog
import com.hjq.gson.factory.GsonFactory
import com.google.gson.JsonSyntaxException
import cn.outter.demo.net.HttpData
import cn.outter.demo.net.exception.TokenException
import cn.outter.demo.net.exception.ResultException
import android.content.Intent
import cn.outter.demo.account.LoginActivity
import android.net.NetworkInfo
import android.net.ConnectivityManager
import cn.outter.demo.net.HttpCacheManager
import com.hjq.http.exception.*
import com.hjq.http.request.HttpRequest
import okhttp3.Headers
import okhttp3.Response
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.lang.reflect.GenericArrayType
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/EasyHttp
 * time   : 2019/05/19
 * desc   : 请求处理类
 */
class RequestHandler(private val mApplication: Application?) : IRequestHandler {
    @Throws(Exception::class)
    override fun requestSuccess(
        httpRequest: HttpRequest<*>, response: Response,
        type: Type
    ): Any {
        if (Response::class.java == type) {
            return response
        }
        if (!response.isSuccessful) {
            throw ResponseException(
                String.format(
                    mApplication!!.getString(R.string.http_response_error),
                    response.code(), response.message()
                ), response
            )
        }
        if (Headers::class.java == type) {
            return response.headers()
        }
        val body = response.body()
            ?: throw NullBodyException(mApplication!!.getString(R.string.http_response_null_body))
        if (ResponseBody::class.java == type) {
            return body
        }

        // 如果是用数组接收，判断一下是不是用 byte[] 类型进行接收的
        if (type is GenericArrayType) {
            val genericComponentType = type.genericComponentType
            if (Byte::class.javaPrimitiveType == genericComponentType) {
                return body.bytes()
            }
        }
        if (InputStream::class.java == type) {
            return body.byteStream()
        }
        if (Bitmap::class.java == type) {
            return BitmapFactory.decodeStream(body.byteStream())
        }
        val text: String
        text = try {
            body.string()
        } catch (e: IOException) {
            // 返回结果读取异常
            throw DataException(mApplication!!.getString(R.string.http_data_explain_error), e)
        }

        // 打印这个 Json 或者文本
        EasyLog.printJson(httpRequest, text)
        if (String::class.java == type) {
            return text
        }
        val result: Any
        result = try {
            GsonFactory.getSingletonGson().fromJson<Any>(text, type)
        } catch (e: JsonSyntaxException) {
            // 返回结果读取异常
            throw DataException(mApplication!!.getString(R.string.http_data_explain_error), e)
        }
        if (result is HttpData<*>) {
            val model = result
            model.responseHeaders = response.headers()
            if (model.isRequestSuccess) {
                // 代表执行成功
                return result
            }
            if (model.isTokenInvalidation) {
                // 代表登录失效，需要重新登录
                throw TokenException(mApplication!!.getString(R.string.http_token_error))
            }
            throw ResultException(model.message, model)
        }
        return result
    }

    override fun requestFail(httpRequest: HttpRequest<*>, e: Exception): Exception {
        if (e is HttpException) {
            if (e is TokenException) {
                // 登录信息失效，跳转到登录页
                if (mApplication != null) {
                    val intent = Intent(mApplication, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    mApplication.startActivity(intent)
                }
            }
            return e
        }
        if (e is SocketTimeoutException) {
            return TimeoutException(mApplication!!.getString(R.string.http_server_out_time), e)
        }
        if (e is UnknownHostException) {
            val info =
                (mApplication!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
            // 判断网络是否连接
            return if (info != null && info.isConnected) {
                // 有连接就是服务器的问题
                ServerException(
                    mApplication.getString(R.string.http_server_error),
                    e
                )
            } else NetworkException(mApplication.getString(R.string.http_network_error), e)
            // 没有连接就是网络异常
        }
        return if (e is IOException) {
            // 出现该异常的两种情况
            // 1. 调用 EasyHttp.cancel
            // 2. 网络请求被中断
            CancelException(mApplication!!.getString(R.string.http_request_cancel), e)
        } else HttpException(e.message, e)
    }

    override fun downloadFail(httpRequest: HttpRequest<*>, e: Exception): Exception {
        if (e is ResponseException) {
            val responseException = e
            val response = responseException.response
            responseException.setMessage(
                String.format(
                    mApplication!!.getString(R.string.http_response_error),
                    response.code(), response.message()
                )
            )
            return responseException
        } else if (e is NullBodyException) {
            val nullBodyException = e
            nullBodyException.setMessage(mApplication!!.getString(R.string.http_response_null_body))
            return nullBodyException
        } else if (e is FileMd5Exception) {
            val fileMd5Exception = e
            fileMd5Exception.setMessage(mApplication!!.getString(R.string.http_response_md5_error))
            return fileMd5Exception
        }
        return requestFail(httpRequest, e)
    }

    override fun readCache(httpRequest: HttpRequest<*>, type: Type, cacheTime: Long): Any? {
        val cacheKey = generateCacheKey(httpRequest)
        val cacheValue = mmkv!!.getString(cacheKey, null)
        if (cacheValue == null || "" == cacheValue || "{}" == cacheValue) {
            return null
        }
        EasyLog.printLog(httpRequest, "----- readCache cacheKey -----")
        EasyLog.printJson(httpRequest, cacheKey)
        EasyLog.printLog(httpRequest, "----- readCache cacheValue -----")
        EasyLog.printJson(httpRequest, cacheValue)
        return GsonFactory.getSingletonGson().fromJson(cacheValue, type)
    }

    override fun writeCache(httpRequest: HttpRequest<*>, response: Response, result: Any): Boolean {
        val cacheKey = generateCacheKey(httpRequest)
        val cacheValue = GsonFactory.getSingletonGson().toJson(result)
        if (cacheValue == null || "" == cacheValue || "{}" == cacheValue) {
            return false
        }
        EasyLog.printLog(httpRequest, "----- writeCache cacheKey -----")
        EasyLog.printJson(httpRequest, cacheKey)
        EasyLog.printLog(httpRequest, "----- writeCache cacheValue -----")
        EasyLog.printJson(httpRequest, cacheValue)
        return mmkv!!.putString(cacheKey, cacheValue).commit()
    }

    override fun clearCache() {
        mmkv!!.clearMemoryCache()
        mmkv!!.clearAll()
    }
}