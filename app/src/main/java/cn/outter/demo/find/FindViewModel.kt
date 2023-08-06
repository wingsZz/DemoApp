package cn.outter.demo.find

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.find.api.FindUserApi
import cn.outter.demo.net.HttpData
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.http.request.HttpRequest
import java.lang.Exception

class FindViewModel:BaseViewModel() {
    val findUserLiveData = MutableLiveData<FindUserApi.FindUserList?>()

    fun getFindUsers(lifecycleOwner: LifecycleOwner) {
        EasyHttp.post(lifecycleOwner)
            .api(FindUserApi())
            .request(object : OnHttpListener<HttpData<FindUserApi.FindUserList?>> {
                override fun onHttpSuccess(result: HttpData<FindUserApi.FindUserList?>) {
                    if (result.data == null) {
                        errorMessageLiveData.value = "没有更多推荐了"
                    } else {
                        findUserLiveData.value = result.data
                    }
                }

                override fun onHttpFail(e: Exception?) {
                    errorMessageLiveData.value = e?.message
                }

            })
    }

}