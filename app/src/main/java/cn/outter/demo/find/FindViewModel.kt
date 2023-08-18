package cn.outter.demo.find

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.find.api.FindUserApi
import cn.outter.demo.find.api.OperationApi
import cn.outter.demo.net.HttpData
import com.google.gson.Gson
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.http.request.HttpRequest
import java.lang.Exception

class FindViewModel : BaseViewModel() {
    val findUserLiveData = MutableLiveData<FindUserApi.FindUserList?>()

    private val findUserApi = FindUserApi()
    private val operationApi = OperationApi()
    fun getFindUsers(lifecycleOwner: LifecycleOwner) {
        EasyHttp.post(lifecycleOwner)
            .api(findUserApi)
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

    fun like(user: FindUserApi.FindUser, lifecycleOwner: LifecycleOwner) {
        EasyHttp.post(lifecycleOwner)
            .api(operationApi)
            .json(Gson().toJson(OperationApi.OperationRequest(user.id, 1)))
            .request(object : OnHttpListener<HttpData<FindUserApi.FindUserList?>> {
                override fun onHttpSuccess(result: HttpData<FindUserApi.FindUserList?>) {
                }

                override fun onHttpFail(e: Exception?) {
                }
            })
    }

    fun dislike(user: FindUserApi.FindUser, lifecycleOwner: LifecycleOwner) {
        EasyHttp.post(lifecycleOwner)
            .api(operationApi)
            .json(Gson().toJson(OperationApi.OperationRequest(user.id, 0)))
            .request(object : OnHttpListener<HttpData<FindUserApi.FindUserList?>> {
                override fun onHttpSuccess(result: HttpData<FindUserApi.FindUserList?>) {

                }

                override fun onHttpFail(e: Exception?) {

                }
            })
    }

}