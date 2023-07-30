package cn.outter.demo.account

import android.os.Bundle
import cn.outter.demo.databinding.OutterActSignupBinding
import me.hgj.jetpackmvvm.base.activity.BaseVmVbActivity

class RegisterActivity:BaseVmVbActivity<RegisterViewModel,OutterActSignupBinding>() {
    override fun createObserver() {

    }

    override fun dismissLoading() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.register.setOnClickListener {
            val account = mViewBind.account.text.toString()
            val password = mViewBind.password.text.toString()
            val passwordAgain = mViewBind.passwordAgain.text.toString()
            if (password != passwordAgain) {
                return@setOnClickListener
            }

        }
    }

    override fun showLoading(message: String) {

    }
}