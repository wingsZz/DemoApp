package cn.outter.demo.splash

import androidx.lifecycle.MutableLiveData
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.bean.User
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import io.reactivex.Flowable
import io.reactivex.FlowableSubscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscription

class SplashViewModel : BaseViewModel() {
    val userLiveData = MutableLiveData<User?>()

    fun chooseWhereToGo() {
        Flowable.just(DataCacheInMemory.MINE_KEY)
            .map {
                MMKV.defaultMMKV().getString(it, "")
            }
            .map {
                Gson().fromJson(it, User::class.java)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : FlowableSubscriber<User> {
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