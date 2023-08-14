package cn.outter.demo.conversation

import android.annotation.SuppressLint
import android.app.Service
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.GlideEngine
import cn.outter.demo.base.BaseVmVbFragment
import cn.outter.demo.database.entity.ChatUser
import cn.outter.demo.database.entity.Message
import cn.outter.demo.database.entity.MsgType
import cn.outter.demo.database.entity.Session
import cn.outter.demo.databinding.OutterFragmentConversationBinding
import cn.outter.demo.keyboard.KeyboardHeightObserver
import cn.outter.demo.keyboard.KeyboardHeightProvider
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener


class ConversionFragment :
    BaseVmVbFragment<ConversationViewModel, OutterFragmentConversationBinding>(),
    KeyboardHeightObserver {

    companion object {
        val TAG = "ConversionFragment"
    }

    private var toUser: ChatUser? = null
    private val mine = DataCacheInMemory.mine

    private var adapter: MessageAdapter? = null

    private var heightProvider: KeyboardHeightProvider? = null
    private var inputManager: InputMethodManager? = null
    private var isPanelShow = false
    private var keyboardHeight = 900

    override fun createObserver() {
        mViewModel.sessionLiveData.observe(this) {
            if (it != null && it.id.isNotEmpty()) {
                mViewModel.getConversations(System.currentTimeMillis())
            } else {
                requireActivity().finish()
            }
        }

        mViewModel.messagesLiveData.observe(this) {
            mViewBind.refreshLayout.isRefreshing = false
            val isInit = (adapter?.itemCount ?: 0) == 0
            adapter?.addAll(it)
            if (isInit) {
                if (adapter != null && adapter!!.itemCount > 0) {
                    mViewBind.contentRyv.scrollToPosition(adapter!!.itemCount - 1)
                }
            }
        }

        mViewModel.remoteMessageLiveData.observe(this) {
            mViewBind.refreshLayout.isRefreshing = false
            adapter?.addAll(it)
            if (adapter != null && adapter!!.itemCount > 0) {
                mViewBind.contentRyv.scrollToPosition(adapter!!.itemCount - 1)
            }
        }
    }

    override fun dismissLoading() {

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView(savedInstanceState: Bundle?) {
        inputManager =
            context?.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager?
        mViewBind.contentRyv.setOnTouchListener(OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                if (inputManager?.isActive(mViewBind.sendEdt) == true) {
                    inputManager?.hideSoftInputFromWindow(mViewBind.sendEdt.windowToken, 0)
                }
                mViewBind.sendEdt.clearFocus()

                if (isPanelShow) {
                    layoutPanelHeight(1)
                    isPanelShow = false
                }
            }
            false
        })
        mViewBind.back.setOnClickListener {
            requireActivity().finish()
        }
        mViewBind.switchButton.setOnClickListener {
            isPanelShow = !isPanelShow
            if (isPanelShow) {
                if (inputManager?.isActive(mViewBind.sendEdt) == true) {
                    inputManager?.hideSoftInputFromWindow(mViewBind.sendEdt.windowToken, 0)
                }
                mViewBind.sendEdt.clearFocus()
                layoutPanelHeight(keyboardHeight)
            } else {
                inputManager?.showSoftInput(mViewBind.sendEdt, InputMethodManager.SHOW_FORCED)
                mViewBind.sendEdt.requestFocus()
            }
        }
        mViewBind.send.setOnClickListener {
            val text = mViewBind.sendEdt.text.toString()
            val message =
                MsgFactory.createTxtMsg(text, toUser!!.userId, mine?.id ?: "", true)
            mViewModel.sendTxtMessage(
                text, message, this@ConversionFragment
            )
            mViewBind.sendEdt.setText("")
            adapter?.add(message)
            mViewBind.contentRyv.scrollToPosition((adapter?.items?.size ?: 1) - 1)
        }
        mViewBind.imagePicker.setOnClickListener {
            PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setMaxSelectNum(1)
                .isMaxSelectEnabledMask(true)
                .isDisplayCamera(false)
                .setImageEngine(GlideEngine.InstanceHolder.instance)
                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: ArrayList<LocalMedia?>) {
                        if (result.isNotEmpty()) {
                            result.forEach {
                                Log.d(TAG, "result = ${it?.path}")
                                if (it?.path.isNullOrEmpty()) {
                                    return@forEach
                                }
                                val message = MsgFactory.createPicMsg(
                                    it!!.path,
                                    "",
                                    it.width,
                                    it.height,
                                    toUser!!.userId,
                                    mine?.id ?: "", true
                                )
                                mViewModel.sendPicMessage(it.path, message, this@ConversionFragment)
                                adapter?.add(message)
                                mViewBind.contentRyv.scrollToPosition(
                                    (adapter?.items?.size ?: 1) - 1
                                )
                            }
                        }
                    }

                    override fun onCancel() {
                        Log.d(TAG, "onCancel")
                    }
                })
        }

        adapter = MessageAdapter()
        adapter?.setOnItemClickListener(object :BaseQuickAdapter.OnItemClickListener<Message>{
            override fun onClick(
                adapter: BaseQuickAdapter<Message, *>,
                view: View,
                position: Int
            ) {
                Log.d(TAG,"message clicked!")
                val message = adapter.items[position]
                if (message.msgType == MsgType.PIC) {
                    // 如果不是会员，点击弹出充值
                }
            }

        })
        mViewBind.contentRyv.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewBind.contentRyv.adapter = adapter
        mViewBind.refreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189))
        mViewBind.refreshLayout.setOnRefreshListener {
            if (adapter?.items?.isEmpty() == true) {
                mViewBind.refreshLayout.isRefreshing = false
                return@setOnRefreshListener
            }

            mViewModel.getConversations(
                adapter?.items?.get((adapter?.itemCount ?: 0) - 1)?.sendTime ?: -1
            )
        }

        heightProvider = KeyboardHeightProvider(requireActivity())
    }

    override fun lazyLoadData() {
        toUser = arguments?.get("toUser") as ChatUser?
        if (toUser == null) {
            requireActivity().finish()
            return
        }
        mViewBind.userName.text = toUser!!.userName
        Glide.with(requireActivity()).load(toUser?.avatarUrl).circleCrop()
            .into(mViewBind.userAvatar)
        adapter?.setChatUser(toUser)
        val session = arguments?.get("session") as Session?
        if (session == null) {
            mViewModel.getSession(toUser!!)
        } else {
            mViewModel.setSession(session)
        }
    }

    override fun onResume() {
        super.onResume()
        mViewBind.contentRyv.postDelayed({
            heightProvider?.start()
        }, 100)
        heightProvider?.setKeyboardHeightObserver(this)
    }

    override fun onPause() {
        super.onPause()
        heightProvider?.setKeyboardHeightObserver(null)
    }

    override fun showLoading(message: String) {

    }

    override fun onKeyboardHeightChanged(height: Int, orientation: Int) {
        if (height <= 20) {
            processKeyboardHide()
        } else if (height >= 200) {
            keyboardHeight = height
            processKeyboardShow(height)
        }
    }

    private fun processKeyboardHide() {
        if (!isPanelShow) {
            layoutPanelHeight(1)
        }
    }

    private fun processKeyboardShow(height: Int) {
        layoutPanelHeight(height)
        isPanelShow = false
    }

    private fun layoutPanelHeight(height: Int) {
        val param = mViewBind.panel.layoutParams
        param.height = height
        mViewBind.panel.requestLayout()
        mViewBind.contentRyv.scrollToPosition((adapter?.items?.size ?: 1) - 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        heightProvider?.close()
    }
}