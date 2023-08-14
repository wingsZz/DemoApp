package cn.outter.demo.account

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.account.api.LoginApi
import cn.outter.demo.account.api.UserInfoApi
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.bean.User
import cn.outter.demo.net.HttpData
import com.google.gson.Gson
import com.hjq.http.EasyConfig
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener

class LoginViewModel : BaseViewModel() {

    private val loginApi = LoginApi()
    private val userInfoApi = UserInfoApi()
    val userLiveData = MutableLiveData<LoginApi.LoginResponse?>()
    val userInfoLiveData = MutableLiveData<UserInfoApi.UserInfo?>()

    fun login(account: String, password: String, owner: LifecycleOwner) {
        EasyHttp.post(owner)
            .json(Gson().toJson(LoginApi.LoginRequest(account, password)))
            .api(
                loginApi
            )
            .request(object : OnHttpListener<HttpData<LoginApi.LoginResponse?>> {
                override fun onHttpSuccess(result: HttpData<LoginApi.LoginResponse?>) {
                    if (result.data == null) {
                        userLiveData.value = null
                        errorMessageLiveData.value = "获取用户信息失败，请重试!"
                    } else {
                        EasyConfig.getInstance().addHeader("token", result.data.token)
                        userLiveData.value = result.data
                    }
                }

                override fun onHttpFail(e: Exception?) {
                    userLiveData.value = null
                    errorMessageLiveData.value = e?.message
                }

            })
    }

    fun getUserInfo(owner: LifecycleOwner) {
        EasyHttp.post(owner)
            .api(userInfoApi)
            .request(object : OnHttpListener<HttpData<UserInfoApi.UserInfo?>> {
                override fun onHttpSuccess(result: HttpData<UserInfoApi.UserInfo?>) {
                    if (result.data == null) {
                        userInfoLiveData.value = null
                        errorMessageLiveData.value = "获取用户信息失败，请重试!"
                    } else {
                        val user = User(
                            "",
                            result.data.username,
                            EasyConfig.getInstance().headers["token"]?:"",
                            result.data.nickname,
                            result.data.gender,
                            result.data.birthday,
                            result.data.avatarUrl,
                            result.data.photoUrl
                        )
                        DataCacheInMemory.refreshMine(user)
                        userInfoLiveData.value = result.data
                    }
                }

                override fun onHttpFail(e: java.lang.Exception?) {

                }

            })
    }
}