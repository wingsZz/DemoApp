package cn.outter.demo.account

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.DatePicker.OnDateChangedListener
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.base.BaseVmVbActivity
import cn.outter.demo.databinding.OutterActSignupBinding
import cn.outter.demo.utils.DateFormatUtil
import cn.outter.demo.widget.picker.DateTimeConfig
import cn.outter.demo.widget.picker.dialog.CardDatePickerDialog
import com.gyf.immersionbar.ImmersionBar
import com.hjq.http.EasyConfig
import com.hjq.toast.ToastUtils
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class RegisterActivity: BaseVmVbActivity<RegisterViewModel, OutterActSignupBinding>() {
    override fun createObserver() {
        mViewModel.userLiveData.observe(this) {
            dismissLoading()
            if (it == null || it.token.isNullOrEmpty()) {
                ToastUtils.show("注册失败，请重试")
            } else {
                EasyConfig.getInstance().addHeader("token",it.token)
                finish()
            }
        }

        mViewModel.errorMessageLiveData.observe(this) {
            dismissLoading()
            ToastUtils.show(it)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.register.setOnClickListener {
            showLoading("正在注册")
            val account = mViewBind.account.text.toString()
            if (account.isEmpty()) {
                ToastUtils.show("请输入用户名!")
                return@setOnClickListener
            }
            val password = mViewBind.password.text.toString()
            val passwordAgain = mViewBind.passwordAgain.text.toString()
            if (password != passwordAgain) {
                ToastUtils.show("两次输入的密码不一致，请重新输入")
                return@setOnClickListener
            }
            val birthday = mViewBind.date.text.toString()
            if (birthday.isEmpty()) {
                ToastUtils.show("请选择生日!")
                return@setOnClickListener
            }
            mViewModel.registerAccount(account,password,birthday,this@RegisterActivity)
        }

        mViewBind.date.setOnClickListener {
            val year = Calendar.getInstance().get(Calendar.YEAR) - 18
            val month = Calendar.getInstance().get(Calendar.MONTH)
            val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            CardDatePickerDialog.Builder(this@RegisterActivity)
                .setTitle("请选择生日")
                .setDisplayType(*intArrayOf(
                    DateTimeConfig.YEAR,
                    DateTimeConfig.MONTH,
                    DateTimeConfig.DAY,
                ))
                .setMaxTime(GregorianCalendar(year, month, day).timeInMillis)
                .setOnChoose {
                    mViewBind.date.text = DateFormatUtil.formatTimeString(it,"yyyy-MM-dd")
                }
                .showBackNow(false)
                .setOnCancel {

                }
                .build().show()
        }

    }
}