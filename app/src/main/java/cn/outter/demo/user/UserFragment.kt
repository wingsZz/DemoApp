package cn.outter.demo.user

import android.content.Intent
import android.os.Bundle
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.account.LoginActivity
import cn.outter.demo.base.BaseVmVbFragment
import cn.outter.demo.bean.User
import cn.outter.demo.databinding.OutterFragUserBinding
import com.bumptech.glide.Glide
import com.hjq.http.EasyConfig

class UserFragment:BaseVmVbFragment<UserViewModel,OutterFragUserBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.logout.setOnClickListener {
            mViewModel.logout(this)
        }

        mViewBind.modifyBirth.setOnClickListener {

        }

        mViewBind.modifyPassword.setOnClickListener {

        }

        mViewBind.modifyNickName.setOnClickListener {

        }
    }

    override fun lazyLoadData() {
        if (DataCacheInMemory.mine == null) {
            mViewModel.getUserInfo(this@UserFragment)
        } else {
            mapUserInfo(DataCacheInMemory.mine!!)
        }
    }

    override fun createObserver() {
        mViewModel.userInfoLiveData.observe(this) {
            if (it != null) {
                mapUserInfo(it)
            } else {

            }
        }
        mViewModel.logoutLiveData.observe(this) {
            startActivity(Intent(requireContext(),LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun mapUserInfo(user:User) {
        mViewBind.userName.text = user.nickname
        Glide.with(this).load(user.avatarUrl).circleCrop().into(mViewBind.userAvatar)
        Glide.with(this).load(user.photoUrl).into(mViewBind.userBackground)
    }

    override fun showLoading(message: String) {

    }

    override fun dismissLoading() {

    }
}