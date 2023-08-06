package cn.outter.demo.account

import android.content.Intent
import android.os.Bundle
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.MainActivity
import cn.outter.demo.base.BaseVmVbActivity
import cn.outter.demo.databinding.OutterActLoginBinding
import com.hjq.http.EasyConfig

class LoginActivity : BaseVmVbActivity<LoginViewModel, OutterActLoginBinding>() {
    override fun createObserver() {
        mViewModel.userLiveData.observe(this) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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