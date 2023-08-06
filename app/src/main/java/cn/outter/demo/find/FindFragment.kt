package cn.outter.demo.find

import android.content.Intent
import android.os.Bundle
import cn.outter.demo.base.BaseVmVbFragment
import cn.outter.demo.conversation.ConversationActivity
import cn.outter.demo.databinding.OutterFragFindBinding
import cn.outter.demo.find.api.FindUserApi
import cn.outter.demo.widget.card.CardLayoutManager
import cn.outter.demo.widget.card.CardSetting
import cn.outter.demo.widget.card.CardTouchHelperCallback
import cn.outter.demo.widget.card.ReItemTouchHelper

class FindFragment : BaseVmVbFragment<FindViewModel, OutterFragFindBinding>() {
    private var adapter: FindAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        adapter = FindAdapter(ArrayList())
        adapter?.setOnLikeClick(object : OnLikeClick {
            override fun onLikeClick(findUser: FindUserApi.FindUser?) {
                val intent = Intent(requireContext(), ConversationActivity::class.java)
                intent.putExtra("toUserId", findUser?.id)
                startActivity(intent)
            }
        })
        val config = CardSetting()
        mViewBind.userListView.layoutManager = CardLayoutManager(
            ReItemTouchHelper(
                CardTouchHelperCallback(
                    mViewBind.userListView,
                    adapter?.items!!,
                    config
                )
            ), config
        )
        mViewBind.userListView.adapter = adapter
    }

    override fun lazyLoadData() {
        mViewModel.getFindUsers(this@FindFragment)
    }

    override fun createObserver() {
        mViewModel.findUserLiveData.observe(this) {
            if (it != null) {
                adapter?.addAll(it.userList)
            }
        }
    }

    override fun showLoading(message: String) {

    }

    override fun dismissLoading() {

    }
}