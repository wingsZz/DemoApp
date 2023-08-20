package cn.outter.demo.conversation

import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import cn.outter.demo.R
import cn.outter.demo.base.BaseViewModel
import cn.outter.demo.base.BaseVmVbActivity
import cn.outter.demo.databinding.OutterActivityConversationBinding
import com.gyf.immersionbar.ImmersionBar

class ConversationActivity : BaseVmVbActivity<BaseViewModel, OutterActivityConversationBinding>() {
    private var conversationFragment: ConversionFragment? = null

    override fun createObserver() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
//        ImmersionBar.with(this)
//            .statusBarColor(android.R.color.white)
//            .navigationBarColor(R.color.white)
//            .init()
    }

    override fun initView(savedInstanceState: Bundle?) {
        conversationFragment = ConversionFragment()
        val bundle = Bundle()
        bundle.putAll(intent.extras)
        conversationFragment?.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.container, conversationFragment!!, "")
            .commit()
    }
}