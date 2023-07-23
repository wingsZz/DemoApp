package cn.outter.demo.account

import android.content.Intent
import android.os.Bundle
import cn.outter.demo.databinding.OutterActLoginBinding
import me.hgj.jetpackmvvm.base.activity.BaseVmVbActivity

class LoginActivity : BaseVmVbActivity<LoginViewModel, OutterActLoginBinding>() {
    override fun createObserver() {

    }

    override fun dismissLoading() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.login.setOnClickListener {

        }

        mViewBind.register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun showLoading(message: String) {

    }
}