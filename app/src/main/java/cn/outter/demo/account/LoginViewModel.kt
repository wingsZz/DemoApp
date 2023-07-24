package cn.outter.demo.account

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.account.api.LoginApi
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.bean.User
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener

class LoginViewModel : BaseViewModel() {

    private val loginApi = LoginApi()
    private val userLiveData = MutableLiveData<User?>()

    fun login(account: String, password: String, owner: LifecycleOwner) {
        EasyHttp.get(owner)
            .api(
                loginApi.setAccount(account)
                    .setPassword(password)
            )
            .request(object : OnHttpListener<User> {
                override fun onHttpSuccess(result: User?) {
                    DataCacheInMemory.refreshMine(result)
                    userLiveData.value = result
                }

                override fun onHttpFail(e: Exception?) {
                    userLiveData.value = null
                }

            })
    }
}