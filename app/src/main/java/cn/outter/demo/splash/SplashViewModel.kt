package cn.outter.demo.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.bean.User
import com.google.gson.Gson
import com.hjq.http.EasyConfig
import com.tencent.mmkv.MMKV
import io.reactivex.Flowable
import io.reactivex.FlowableSubscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscription

class SplashViewModel : BaseViewModel() {
    val userLiveData = MutableLiveData<User?>()

    fun chooseWhereToGo() {
        val str = MMKV.defaultMMKV().getString(DataCacheInMemory.MINE_KEY, "")
        try {
            val mine = Gson().fromJson(str, User::class.java)
            if (mine == null) {
                userLiveData.value = null
            } else {
                DataCacheInMemory.mine = mine
                EasyConfig.getInstance().addHeader("token", mine.token)
                userLiveData.value = mine
            }
        } catch (e: Exception) {
            userLiveData.value = null
        }

//        Flowable.just(DataCacheInMemory.MINE_KEY)
//            .map {
//                MMKV.defaultMMKV().getString(it, "")
//            }
//            .map {
//                Gson().fromJson(it, User::class.java)
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : FlowableSubscriber<User> {
//                override fun onSubscribe(s: Subscription) {
//                    Log.d("Splash", "onSubscribe")
//                }
//
//                override fun onError(t: Throwable?) {
//
//                }
//
//                override fun onComplete() {
//                    Log.d("Splash", "onComplete")
//                }
//
//                override fun onNext(t: User?) {
//
//                }
//
//            })
    }
}