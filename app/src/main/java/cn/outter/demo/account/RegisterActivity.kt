package cn.outter.demo.account

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.DatePicker.OnDateChangedListener
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.base.BaseVmVbActivity
import cn.outter.demo.databinding.OutterActSignupBinding
import com.hjq.http.EasyConfig
import com.hjq.toast.ToastUtils
import java.util.Calendar

class RegisterActivity: BaseVmVbActivity<RegisterViewModel, OutterActSignupBinding>() {
    override fun createObserver() {
        mViewModel.userLiveData.observe(this) {
            if (it == null || it.token.isNullOrEmpty()) {
                Log.d("Register","注册失败")
            } else {
                DataCacheInMemory.refreshMine(it)
                EasyConfig.getInstance().addHeader("token",it.token)
            }
        }

        mViewModel.errorMessageLiveData.observe(this) {
            ToastUtils.show(it)
        }
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

            mViewModel.registerAccount(account,password,this@RegisterActivity)
        }

        initDatePicker()
    }

    private fun initDatePicker() {
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR) - 18
        val monthOfYear: Int = calendar.get(Calendar.MONTH)
        val dayOfMonth: Int = calendar.get(Calendar.DAY_OF_MONTH)
    }

    override fun showLoading(message: String) {

    }
}