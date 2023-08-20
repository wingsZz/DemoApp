package cn.outter.demo.account

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.account.api.RegisterApi
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.bean.User
import cn.outter.demo.net.HttpData
import com.google.gson.Gson
import com.hjq.http.EasyConfig
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener

class RegisterViewModel: BaseViewModel() {
    private val registerApi = RegisterApi()
    val userLiveData = MutableLiveData<User?>()

    fun registerAccount(account:String,password:String,birthday:String,owner: LifecycleOwner) {
        EasyHttp.post(owner)
            .json(Gson().toJson(RegisterApi.RegisterRequest(account,password,0,birthday)))
            .api(
                registerApi
            )
            .request(object : OnHttpListener<HttpData<User?>> {
                override fun onHttpSuccess(result: HttpData<User?>) {
                    if (result.data == null) {
                        userLiveData.value = null
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