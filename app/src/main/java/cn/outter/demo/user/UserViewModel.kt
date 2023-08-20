package cn.outter.demo.user

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.account.api.LoginApi
import cn.outter.demo.account.api.UserInfoApi
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.bean.User
import cn.outter.demo.net.HttpData
import com.hjq.http.EasyConfig
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener

class UserViewModel:BaseViewModel() {
    private val userInfoApi = UserInfoApi()
    val userInfoLiveData = MutableLiveData<UserInfoApi.UserInfo?>()
    private val logoutApi = LoginApi()
    val logoutLiveData = MutableLiveData<Any>()

    fun getUserInfo(owner: LifecycleOwner) {
        EasyHttp.post(owner)
            .api(userInfoApi)
            .request(object : OnHttpListener<HttpData<UserInfoApi.UserInfo?>> {
                override fun onHttpSuccess(result: HttpData<UserInfoApi.UserInfo?>) {
                    if (result.data == null) {
                        userInfoLiveData.value = null
                        errorMessageLiveData.value = "获取用户信息失败，请重试!"
                    } else {
                        userInfoLiveData.value = result.data
                    }
                }

                override fun onHttpFail(e: java.lang.Exception?) {
                    errorMessageLiveData.value = e?.message
                }
            })
    }

    fun logout(owner: LifecycleOwner) {
        EasyHttp.post(owner)
            .api(logoutApi)
            .request(object : OnHttpListener<HttpData<Any>> {
                override fun onHttpSuccess(result: HttpData<Any>) {
                    DataCacheInMemory.refreshMine(null)
                    logoutLiveData.value = ""
                }

                override fun onHttpFail(e: java.lang.Exception?) {
                    DataCacheInMemory.refreshMine(null)
                    logoutLiveData.value = ""
                }

            })
    }
}