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

    override fun dismissLoading() {

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

    override fun showLoading(message: String) {

    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (conversationFragment != null) {
            return conversationFragment?.dispatchKeyEvent(event) ?: super.dispatchKeyEvent(event)
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean, newConfig: Configuration?) {
        if (conversationFragment != null) {
            return conversationFragment!!.onMultiWindowModeChanged(isInMultiWindowMode)
        }
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig)
    }
}