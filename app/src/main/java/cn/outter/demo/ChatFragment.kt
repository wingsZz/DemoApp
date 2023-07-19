package cn.outter.demo

import android.os.Bundle
import cn.outter.demo.databinding.OutterFragmentChatBinding
import me.hgj.jetpackmvvm.base.fragment.BaseVmVbFragment

class ChatFragment:BaseVmVbFragment<ChatViewModel,OutterFragmentChatBinding>() {
    override fun createObserver() {
        mViewModel.data.observe(this) {

        }
    }

    override fun dismissLoading() {

    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoadData() {

    }

    override fun showLoading(message: String) {

    }
}