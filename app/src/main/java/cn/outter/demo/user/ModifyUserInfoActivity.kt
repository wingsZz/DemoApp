package cn.outter.demo.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.outter.demo.account.api.UserInfoApi
import cn.outter.demo.base.BaseVmVbActivity
import cn.outter.demo.databinding.OutterActModifyUserinfoBinding
import cn.outter.demo.utils.DateFormatUtil
import cn.outter.demo.widget.picker.DateTimeConfig
import cn.outter.demo.widget.picker.dialog.CardDatePickerDialog
import com.hjq.toast.ToastUtils
import me.hgj.jetpackmvvm.util.get
import java.util.Calendar
import java.util.GregorianCalendar

class ModifyUserInfoActivity:BaseVmVbActivity<ModifyUserInfoViewModel,OutterActModifyUserinfoBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        val code = intent.getIntExtra("requestCode",-1)
        val userInfo = intent.getSerializableExtra("userInfo") as UserInfoApi.UserInfo?
        if (userInfo == null) {
            finish()
        }
        if (code == -1) {
            finish()
        } else {
            when(code) {
                ModifyUserInfoConstant.MODIFY_USER_BIRTHDAY -> {
                    mViewBind.modifyPasswordLayout.visibility = View.GONE
                    mViewBind.modifyPasswordAgainLayout.visibility = View.GONE
                    mViewBind.modifyNickNameLayout.visibility = View.GONE
                    mViewBind.modifyBirthLayout.visibility = View.VISIBLE
                    mViewBind.date.text = userInfo!!.birthday
                    mViewBind.titleName.text = "修改生日"
                }
                ModifyUserInfoConstant.MODIFY_USER_PASSWORD -> {
                    mViewBind.modifyPasswordLayout.visibility = View.VISIBLE
                    mViewBind.modifyPasswordAgainLayout.visibility = View.VISIBLE
                    mViewBind.modifyNickNameLayout.visibility = View.GONE
                    mViewBind.modifyBirthLayout.visibility = View.GONE
                    mViewBind.titleName.text = "修改密码"
                }
                ModifyUserInfoConstant.MODIFY_USER_NICK_NAME -> {
                    mViewBind.modifyPasswordLayout.visibility = View.GONE
                    mViewBind.modifyPasswordAgainLayout.visibility = View.GONE
                    mViewBind.modifyNickNameLayout.visibility = View.VISIBLE
                    mViewBind.modifyBirthLayout.visibility = View.GONE
                    mViewBind.account.hint = userInfo!!.nickname
                    mViewBind.titleName.text = "修改昵称"
                }
            }
        }

        mViewBind.date.setOnClickListener {
            val year = Calendar.getInstance().get(Calendar.YEAR) - 18
            val month = Calendar.getInstance().get(Calendar.MONTH) + 1
            val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            CardDatePickerDialog.Builder(this@ModifyUserInfoActivity)
                .setTitle("请选择生日")
                .setDisplayType(*intArrayOf(
                    DateTimeConfig.YEAR,
                    DateTimeConfig.MONTH,
                    DateTimeConfig.DAY,
                ))
                .setMaxTime(GregorianCalendar(year, month, day).timeInMillis)
                .setOnChoose {
                    val birth = DateFormatUtil.formatTimeString(it,"yyyy-MM-dd")
                    mViewBind.date.text = birth

                }
                .showBackNow(false)
                .setOnCancel {

                }
                .build().show()
        }


        mViewBind.modify.setOnClickListener {
            when(code) {
                ModifyUserInfoConstant.MODIFY_USER_BIRTHDAY -> {
                    userInfo!!.birthday = mViewBind.date.text.toString()
                }
                ModifyUserInfoConstant.MODIFY_USER_PASSWORD -> {

                }
                ModifyUserInfoConstant.MODIFY_USER_NICK_NAME -> {
                   userInfo!!.nickname = mViewBind.account.text.toString()
                }
            }
            mViewModel.modifyUserInfo(this@ModifyUserInfoActivity,userInfo!!)
        }

        mViewBind.back.setOnClickListener {
            finish()
        }
    }

    override fun createObserver() {
        mViewModel.modifyLiveData.observe(this) {
            if (it != null) {
                val code = intent.getIntExtra("requestCode",-1)
                val data = Intent()
                data.putExtra("userInfo",it)
                setResult(code,data)
                finish()
            } else {
                ToastUtils.show("修改信息失败，请重试！")
            }
        }
    }
}