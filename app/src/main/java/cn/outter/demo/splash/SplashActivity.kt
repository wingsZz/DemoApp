package cn.outter.demo.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import cn.outter.demo.MainActivity
import cn.outter.demo.account.LoginActivity
import cn.outter.demo.account.RegisterActivity
import cn.outter.demo.base.BaseVmVbActivity
import cn.outter.demo.databinding.OutterActSplashBinding
import com.gyf.immersionbar.ImmersionBar

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseVmVbActivity<SplashViewModel, OutterActSplashBinding>() {
    override fun createObserver() {
        mViewModel.userLiveData.observe(this) {
            mViewBind.root.postDelayed({
                if (it == null) {
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                finish()
            }, 1500)
        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).transparentStatusBar().transparentNavigationBar().init()
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onResume() {
        super.onResume()
        mViewModel.chooseWhereToGo()
    }
}