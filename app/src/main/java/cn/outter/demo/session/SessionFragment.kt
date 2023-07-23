package cn.outter.demo.session

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.outter.demo.ChatViewModel
import cn.outter.demo.databinding.OutterFragmentSessionBinding
import me.hgj.jetpackmvvm.base.fragment.BaseVmVbFragment

class SessionFragment : BaseVmVbFragment<ChatViewModel, OutterFragmentSessionBinding>() {
    private var adapter: SessionAdapter? = null

    override fun createObserver() {
        mViewModel.sessions.observe(this) {
            if (it.isNullOrEmpty()) {

            } else {
                adapter?.setList(it)
            }
        }
    }

    override fun dismissLoading() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        adapter = SessionAdapter()
        mViewBind.sessionListView.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        mViewBind.sessionListView.adapter = adapter
    }

    override fun lazyLoadData() {
        mViewModel.getAllSession()
    }

    override fun showLoading(message: String) {

    }
}