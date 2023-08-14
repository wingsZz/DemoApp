package cn.outter.demo.session

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.outter.demo.base.BaseVmVbFragment
import cn.outter.demo.conversation.ConversationActivity
import cn.outter.demo.database.entity.Session
import cn.outter.demo.databinding.OutterFragmentSessionBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jeremyliao.liveeventbus.LiveEventBus
import java.util.Arrays
import java.util.Collections

class SessionFragment : BaseVmVbFragment<SessionViewModel, OutterFragmentSessionBinding>() {
    private var adapter: SessionAdapter? = null

    override fun createObserver() {
        mViewModel.sessions.observe(this) {
            if (it.isNullOrEmpty()) {

            } else {
                adapter?.addAll(it)
            }
        }
    }

    override fun dismissLoading() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        adapter = SessionAdapter()
        mViewBind.sessionListView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewBind.sessionListView.adapter = adapter

        adapter?.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener<Session> {
            override fun onClick(adapter: BaseQuickAdapter<Session, *>, view: View, position: Int) {
                val session = adapter.items[position] as Session?
                if (session != null) {
                    val intent = Intent(context, ConversationActivity::class.java)
                    intent.putExtra("session", session)
                    intent.putExtra("toUser", session.chatUser)
                    startActivity(intent)
                    mViewBind.sessionListView.postDelayed({
                        mViewModel.updateSessionLastMessageTime(session)
                        Collections.swap(adapter.items, 0, position)
                        adapter.notifyItemMoved(position, 0)
                        mViewBind.sessionListView.scrollToPosition(0)
                    }, 100)
                }
            }
        })

        LiveEventBus.get("", SessionAction::class.java)
            .observeForever {
                val datas = adapter?.items
                if (datas != null) {
                    when (it.type) {
                        0 -> {
                            Log.d("Session","add Session --> ${it.session}")
                            adapter?.add(0, it.session)
                        }
//                        1 -> {
//                            datas.sortWith(Comparator { o1, o2 ->
//                                (o1?.lastMessageTime ?: 0 - (o2?.lastMessageTime ?: 0)).toInt()
//                            })
//                            adapter?.notifyDataSetChanged()
//                        }
                    }
                }
            }
    }

    override fun lazyLoadData() {
        mViewModel.getAllSession()
    }

    override fun showLoading(message: String) {

    }
}