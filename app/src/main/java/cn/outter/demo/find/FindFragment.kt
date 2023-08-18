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
    private var touchHelper: ReItemTouchHelper? = null

    override fun initView(savedInstanceState: Bundle?) {
        adapter = FindAdapter(ArrayList())
        val config = CardSetting()
        config.setSwipeListener(object : OnSwipeCardListener<FindUserApi.FindUser> {
            override fun onSwiping(
                viewHolder: RecyclerView.ViewHolder?,
                dx: Float,
                dy: Float,
                direction: Int
            ) {
                Log.d(TAG, "onSwiping dx = $dx,dy = $dy,direction = $direction")
                when (direction) {
                    ReItemTouchHelper.DOWN -> {

                    }

                    ReItemTouchHelper.UP -> {

                    }

                    ReItemTouchHelper.RIGHT -> {
                        // like
                    }

                    ReItemTouchHelper.LEFT -> {
                        //dislike
                    }
                }
            }

            override fun onSwipedClear() {
                Log.d(TAG, "onSwipedClear")
            }

            override fun onSwipedOut(
                viewHolder: RecyclerView.ViewHolder?,
                t: FindUserApi.FindUser?,
                direction: Int
            ) {
                Log.d(TAG, "onSwipedOut data = $t,direction = $direction")
                if (t != null) {
                    when (direction) {
                        ReItemTouchHelper.DOWN -> {

                        }

                        ReItemTouchHelper.UP -> {

                        }

                        ReItemTouchHelper.RIGHT -> {
                            // like
                            mViewModel.like(t, this@FindFragment)
                        }

                        ReItemTouchHelper.LEFT -> {
                            //dislike
                            mViewModel.dislike(t, this@FindFragment)
                        }
                    }
                }
            }

        })
        touchHelper = ReItemTouchHelper(
            CardTouchHelperCallback(
                mViewBind.userListView,
                adapter?.items!!,
                config
            )
        )
        mViewBind.userListView.layoutManager = CardLayoutManager(
            touchHelper!!, config
        )
        mViewBind.userListView.adapter = adapter

        mViewBind.superlike.setOnClickListener {
            val currentUser = adapter?.items?.get(0)
            if (currentUser != null) {
                Log.d(TAG, "currentUser = $currentUser")
                val chatUser =
                    ChatUser(currentUser.id, currentUser.name ?: "", currentUser.avatarUrl ?: "")
                val intent = Intent(requireContext(), ConversationActivity::class.java)
                intent.putExtra("toUser", chatUser)
                startActivity(intent)
            }
        }

        mViewBind.dislike.setOnClickListener {
            if ((adapter?.itemCount ?: 0) > 0) {
                touchHelper?.swipeManually(ReItemTouchHelper.LEFT)
            }
        }

        mViewBind.like.setOnClickListener {
            if ((adapter?.itemCount ?: 0) > 0) {
                touchHelper?.swipeManually(ReItemTouchHelper.RIGHT)
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