package cn.outter.demo.splash

import androidx.lifecycle.MutableLiveData
import cn.outter.demo.bean.User
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import io.reactivex.Flowable
import io.reactivex.FlowableSubscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import org.reactivestreams.Subscription

class SplashViewModel:BaseViewModel() {
    val userLiveData = MutableLiveData<User?>()

    fun chooseWhereToGo() {
        Flowable.just("")
            .map {
                MMKV.defaultMMKV().getString("","")
            }
            .map {
                Gson().fromJson(it,User::class.java)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :FlowableSubscriber<User>{
                override fun onSubscribe(s: Subscription) {

                }

                override fun onError(t: Throwable?) {
                    userLiveData.value = null
                }

                override fun onComplete() {

                }

                override fun onNext(t: User?) {
                    userLiveData.value = t
                }

            })
    }
}