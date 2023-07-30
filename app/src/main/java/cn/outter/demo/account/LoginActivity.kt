package cn.outter.demo.account

import android.content.Intent
import android.os.Bundle
import cn.outter.demo.base.BaseVmVbActivity
import cn.outter.demo.databinding.OutterActLoginBinding

class LoginActivity : BaseVmVbActivity<LoginViewModel, OutterActLoginBinding>() {
    override fun createObserver() {

    }

    override fun dismissLoading() {

    }

    override fun initView(savedInstanceState: Bundle?) {
//        mViewBind.login.setOnClickListener {
//            val account = mViewBind.account.text.toString()
//            val password = mViewBind.password.text.toString()
//            mViewModel.login(account, password ,this)
//        }

        mViewBind.signUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun showLoading(message: String) {

    }
}