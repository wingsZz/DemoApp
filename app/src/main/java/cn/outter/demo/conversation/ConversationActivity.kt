package cn.outter.demo.conversation

import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import cn.outter.demo.R
import cn.outter.demo.databinding.OutterActivityConversationBinding
import me.hgj.jetpackmvvm.base.activity.BaseVmVbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class ConversationActivity : BaseVmVbActivity<BaseViewModel, OutterActivityConversationBinding>() {
    private var conversationFragment: ConversionFragment? = null

    override fun createObserver() {

    }

    override fun dismissLoading() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?) {
//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        conversationFragment = ConversionFragment()
        supportFragmentManager.beginTransaction().add(R.id.container,conversationFragment!!,"").commit()
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