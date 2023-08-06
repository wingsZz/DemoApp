package cn.outter.demo.account

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.account.api.LoginApi
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.bean.User
import cn.outter.demo.net.HttpData
import com.google.gson.Gson
import com.hjq.http.EasyConfig
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener

class LoginViewModel : BaseViewModel() {

    private val loginApi = LoginApi()
    val userLiveData = MutableLiveData<User?>()

    fun login(account: String, password: String, owner: LifecycleOwner) {
        EasyHttp.post(owner)
            .json(Gson().toJson(LoginApi.LoginRequest(account, password)))
            .api(
                loginApi
            )
            .request(object : OnHttpListener<HttpData<User?>> {
                override fun onHttpSuccess(result: HttpData<User?>) {
                    if (result.data == null) {
                        userLiveData.value = null
                        errorMessageLiveData.value = "获取用户信息失败，请重试!"
                    } else {
                        DataCacheInMemory.refreshMine(result.data)
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
}