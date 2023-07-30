package cn.outter.demo.conversation

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation
import cn.outter.demo.R
import cn.outter.demo.base.BaseVmVbFragment
import cn.outter.demo.bean.User
import cn.outter.demo.database.entity.Session
import cn.outter.demo.databinding.OutterFragmentConversationBinding
import cn.outter.demo.keyboard.util.KPSwitchConflictUtil
import cn.outter.demo.keyboard.util.KeyboardUtil

class ConversionFragment :
    BaseVmVbFragment<ConversationViewModel, OutterFragmentConversationBinding>() {

    private var session:Session? = null
    private var toUserId:String = ""

    private var adapter:MessageAdapter? = null

    override fun createObserver() {
        mViewModel.sessionLiveData.observe(this) {
            if (session != null && session?.id?.isNotEmpty() == true) {
                mViewModel.getConversations(session!!.id,System.currentTimeMillis())
            } else {

            }
        }

        mViewModel.messagesLiveData.observe(this) {
            mViewBind.messageListView.refreshLayout.isRefreshing = false
            adapter?.addAll(it)
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
        mViewBind.messageListView.contentRyv.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,true)
        mViewBind.messageListView.contentRyv.adapter = adapter

        mViewBind.messageListView.refreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189))
        mViewBind.messageListView.refreshLayout.setOnRefreshListener{
            if (adapter?.items?.isEmpty() == true) {
                mViewBind.messageListView.refreshLayout.isRefreshing = false
            }

            mViewModel.getConversations(session?.id?:"",adapter?.items?.get((adapter?.itemCount?:0) - 1)?.sendTime?: -1)
        }
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
            mViewModel.getConversations(session!!.id,System.currentTimeMillis())
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