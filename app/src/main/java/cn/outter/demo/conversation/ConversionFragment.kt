package cn.outter.demo.conversation

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.recyclerview.widget.LinearLayoutManager
import cn.outter.demo.R
import cn.outter.demo.bean.User
import cn.outter.demo.database.entity.Session
import cn.outter.demo.databinding.OutterFragmentConversationBinding
import cn.outter.demo.keyboard.util.KPSwitchConflictUtil
import cn.outter.demo.keyboard.util.KeyboardUtil
import me.hgj.jetpackmvvm.base.fragment.BaseVmVbFragment

class ConversionFragment :
    BaseVmVbFragment<ConversationViewModel, OutterFragmentConversationBinding>() {

    private var session:Session? = null
    private var toUserId:String = ""

    private var adapter:MessageAdapter? = null

    override fun createObserver() {
        mViewModel.sessionLiveData.observe(this) {

        }

        mViewModel.messagesLiveData.observe(this) {

        }
    }

    override fun dismissLoading() {

    }

    override fun initView(savedInstanceState: Bundle?) {

        // ********* Above code Just for Demo Test, do not need to adapt in your code. ************
        KeyboardUtil.attach(activity,
            mViewBind.panelRoot, { isShowing ->
                Log.d(
                    "KeyBoard",
                    String.format("Keyboard is %s", if (isShowing) "showing" else "hiding")
                )
            })


        // In the normal case.
        KPSwitchConflictUtil.attach(
            mViewBind.panelRoot,
            mViewBind.toolBar.plusIv,
            mViewBind.toolBar.sendEdt
        )
        { view, switchToPanel ->
            if (switchToPanel) {
                mViewBind.toolBar.sendEdt.clearFocus()
            } else {
                mViewBind.toolBar.sendEdt.requestFocus()
            }
        }

        mViewBind.messageListView.contentRyv.setOnTouchListener(OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                KPSwitchConflictUtil.hidePanelAndKeyboard(mViewBind.panelRoot)
            }
            false
        })

        adapter = MessageAdapter()
        mViewBind.messageListView.contentRyv.layoutManager = LinearLayoutManager(context)
        mViewBind.messageListView.contentRyv.adapter = adapter
    }

    override fun lazyLoadData() {
//        val toUser = arguments?.get("toUser") as User?
//        if (toUser == null) {
//            requireActivity().finish()
//            return
//        }
        session = arguments?.get("session") as Session?
        toUserId = arguments?.getString("toUserId")?: ""
        if (session == null) {
            mViewModel.getSession(toUserId)
        } else {
            mViewModel.getConversations(session!!.id)
        }
    }

    override fun showLoading(message: String) {

    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean) {
        KPSwitchConflictUtil.onMultiWindowModeChanged(isInMultiWindowMode)
    }

    fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_UP && event.keyCode == KeyEvent.KEYCODE_BACK) {
            KPSwitchConflictUtil.hidePanelAndKeyboard(mViewBind.panelRoot)
            return true
        }
        return false
    }
}