package cn.outter.demo.account

import android.content.Intent
import android.os.Bundle
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.MainActivity
import cn.outter.demo.base.BaseVmVbActivity
import cn.outter.demo.databinding.OutterActLoginBinding
import com.hjq.http.EasyConfig
import com.hjq.toast.ToastUtils

class LoginActivity : BaseVmVbActivity<LoginViewModel, OutterActLoginBinding>() {
    override fun createObserver() {
        mViewModel.userLiveData.observe(this) {
            if (it != null) {
                mViewModel.getUserInfo(this@LoginActivity)
            } else {
                ToastUtils.show("登录失败，请重试")
            }
        }

        mViewModel.userInfoLiveData.observe(this) {
            if (it != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                ToastUtils.show("登录失败，请重试")
            }
        }
    }

    override fun dismissLoading() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.login.setOnClickListener {
            val account = mViewBind.account.text.toString()
            val password = mViewBind.password.text.toString()
            mViewModel.login(account, password, this@LoginActivity)
        }

        mViewBind.signUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun showLoading(message: String) {

    }
}