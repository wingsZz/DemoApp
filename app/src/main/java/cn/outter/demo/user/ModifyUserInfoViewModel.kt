package cn.outter.demo.user

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import cn.outter.demo.account.api.UserInfoApi
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.net.HttpData
import cn.outter.demo.user.api.ModifyUserInfoApi
import com.google.gson.Gson
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import java.lang.Exception

class ModifyUserInfoViewModel:BaseViewModel() {
    private val api = ModifyUserInfoApi()

    val modifyLiveData = MutableLiveData<UserInfoApi.UserInfo?>()

    fun modifyUserInfo(lifecycleOwner: LifecycleOwner,userInfo:UserInfoApi.UserInfo) {
        EasyHttp.post(lifecycleOwner)
            .api(api)
            .json(Gson().toJson(userInfo))
            .request(object :OnHttpListener<HttpData<Any>>{
                override fun onHttpSuccess(result: HttpData<Any>?) {
                    modifyLiveData.value = userInfo
                }

                override fun onHttpFail(e: Exception?) {
                    modifyLiveData.value = null
                }
            })
    }
}