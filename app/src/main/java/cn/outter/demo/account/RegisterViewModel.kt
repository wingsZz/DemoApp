package cn.outter.demo.account

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import cn.outter.demo.account.api.RegisterApi
import cn.outter.demo.bean.User
import com.google.gson.Gson
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class RegisterViewModel:BaseViewModel() {
    private val registerApi = RegisterApi()
    val userLiveData = MutableLiveData<User?>()

    fun registerAccount(account:String,password:String,owner: LifecycleOwner) {
        EasyHttp.post(owner)
            .json(Gson().toJson(RegisterApi.RegisterRequest(account,password)))
            .api(
                registerApi
            )
            .request(object : OnHttpListener<User> {
                override fun onHttpSuccess(result: User?) {
                    userLiveData.value = result
                }

                override fun onHttpFail(e: Exception?) {
                    userLiveData.value = null
                }

            })
    }
}