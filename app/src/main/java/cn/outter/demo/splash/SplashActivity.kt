package cn.outter.demo.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import cn.outter.demo.MainActivity
import cn.outter.demo.account.LoginActivity
import cn.outter.demo.databinding.OutterActSplashBinding
import me.hgj.jetpackmvvm.base.activity.BaseVmVbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity:BaseVmVbActivity<SplashViewModel,OutterActSplashBinding>() {
    override fun createObserver() {
        mViewModel.userLiveData.observe(this) {
            if (it == null) {
                startActivity(Intent(this,LoginActivity::class.java))
            } else {
                startActivity(Intent(this,MainActivity::class.java))
            }
        }
    }

    override fun dismissLoading() {

    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onResume() {
        super.onResume()
        mViewModel.chooseWhereToGo()
    }

    override fun showLoading(message: String) {

    }
}