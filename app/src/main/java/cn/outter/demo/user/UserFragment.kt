package cn.outter.demo.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.account.LoginActivity
import cn.outter.demo.account.api.UserInfoApi
import cn.outter.demo.base.BaseVmVbFragment
import cn.outter.demo.bean.User
import cn.outter.demo.databinding.OutterFragUserBinding
import com.bumptech.glide.Glide
import com.hjq.http.EasyConfig

class UserFragment : BaseVmVbFragment<UserViewModel, OutterFragUserBinding>() {

    private lateinit var modifyUserLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modifyUserLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback {
                    val resultCode = it.resultCode
                    val userInfo =
                        it.data?.getSerializableExtra("userInfo") as UserInfoApi.UserInfo?
                    if (userInfo != null) {
                        Log.d("User", "get result = $userInfo")
                        if (resultCode == ModifyUserInfoConstant.MODIFY_USER_BIRTHDAY
                            || resultCode == ModifyUserInfoConstant.MODIFY_USER_PASSWORD
                            || resultCode == ModifyUserInfoConstant.MODIFY_USER_NICK_NAME) {
                            mViewModel.userInfoLiveData.value = userInfo
                        }
                    }
                })
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.logout.setOnClickListener {
            mViewModel.logout(this)
        }

        mViewBind.modifyBirth.setOnClickListener {
            if (mViewModel.userInfoLiveData.value == null) {
                return@setOnClickListener
            }
            val intent = Intent(requireContext(), ModifyUserInfoActivity::class.java)
            intent.putExtra("userInfo", mViewModel.userInfoLiveData.value)
            intent.putExtra("requestCode", ModifyUserInfoConstant.MODIFY_USER_BIRTHDAY)
            modifyUserLauncher.launch(intent)
        }

        mViewBind.modifyPassword.setOnClickListener {
            if (mViewModel.userInfoLiveData.value == null) {
                return@setOnClickListener
            }
            val intent = Intent(requireContext(), ModifyUserInfoActivity::class.java)
            intent.putExtra("userInfo", mViewModel.userInfoLiveData.value)
            intent.putExtra("requestCode", ModifyUserInfoConstant.MODIFY_USER_PASSWORD)
            modifyUserLauncher.launch(intent)
        }

        mViewBind.modifyNickName.setOnClickListener {
            if (mViewModel.userInfoLiveData.value == null) {
                return@setOnClickListener
            }
            val intent = Intent(requireContext(), ModifyUserInfoActivity::class.java)
            intent.putExtra("userInfo", mViewModel.userInfoLiveData.value)
            intent.putExtra("requestCode", ModifyUserInfoConstant.MODIFY_USER_NICK_NAME)
            modifyUserLauncher.launch(intent)
        }
    }

    override fun lazyLoadData() {
        mViewModel.getUserInfo(this@UserFragment)
    }

    override fun createObserver() {
        mViewModel.userInfoLiveData.observe(this) {
            if (it != null) {
                mapUserInfo(it)
            }
        }
        mViewModel.logoutLiveData.observe(this) {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun mapUserInfo(user: UserInfoApi.UserInfo) {
        mViewBind.userName.text = user.nickname
        Glide.with(this).load(user.avatarUrl).circleCrop().into(mViewBind.userAvatar)
        Glide.with(this).load(user.photoUrl).into(mViewBind.userBackground)
    }

    override fun showLoading(message: String) {

    }

    override fun dismissLoading() {

    }
}