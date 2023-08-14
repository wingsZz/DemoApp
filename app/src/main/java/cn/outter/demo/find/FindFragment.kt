package cn.outter.demo.find

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import cn.outter.demo.base.BaseVmVbFragment
import cn.outter.demo.conversation.ConversationActivity
import cn.outter.demo.database.entity.ChatUser
import cn.outter.demo.databinding.OutterFragFindBinding
import cn.outter.demo.find.api.FindUserApi
import cn.outter.demo.widget.card.CardLayoutManager
import cn.outter.demo.widget.card.CardSetting
import cn.outter.demo.widget.card.CardTouchHelperCallback
import cn.outter.demo.widget.card.OnSwipeCardListener
import cn.outter.demo.widget.card.ReItemTouchHelper

class FindFragment : BaseVmVbFragment<FindViewModel, OutterFragFindBinding>() {
    companion object {
        val TAG = "FindUser"
    }

    private var adapter: FindAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        adapter = FindAdapter(ArrayList())
        adapter?.setOnLikeClick(object : OnLikeClick {
            override fun onLikeClick(findUser: FindUserApi.FindUser?) {

            }
        })
        val config = CardSetting()
        config.setSwipeListener(object :OnSwipeCardListener<FindUserApi.FindUser> {
            override fun onSwiping(
                viewHolder: RecyclerView.ViewHolder?,
                dx: Float,
                dy: Float,
                direction: Int
            ) {
                Log.d(TAG,"onSwiping dx = $dx,dy = $dy,direction = $direction")
            }

            override fun onSwipedClear() {
                Log.d(TAG,"onSwipedClear")
            }

            override fun onSwipedOut(
                viewHolder: RecyclerView.ViewHolder?,
                t: FindUserApi.FindUser?,
                direction: Int
            ) {
                Log.d(TAG,"onSwipedOut data = $t,direction = $direction")
            }

        })
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

        mViewBind.like.setOnClickListener {
            val currentUser = adapter?.items?.get(0)
            if (currentUser != null) {
                Log.d(TAG,"currentUser = $currentUser")
                val chatUser = ChatUser(currentUser.id,currentUser.name,currentUser.avatarUrl)
                val intent = Intent(requireContext(), ConversationActivity::class.java)
                intent.putExtra("toUser", chatUser)
                startActivity(intent)
            }
        }
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