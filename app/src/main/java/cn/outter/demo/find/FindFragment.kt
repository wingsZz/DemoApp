package cn.outter.demo.find

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import cn.outter.demo.base.BaseVmVbFragment
import cn.outter.demo.bean.FindUser
import cn.outter.demo.databinding.OutterFragFindBinding
import cn.outter.demo.widget.card.CardLayoutManager
import cn.outter.demo.widget.card.CardSetting
import cn.outter.demo.widget.card.CardTouchHelperCallback
import cn.outter.demo.widget.card.ReItemTouchHelper
import cn.outter.demo.widget.card.ReItemTouchHelper.Callback

class FindFragment : BaseVmVbFragment<FindViewModel, OutterFragFindBinding>() {
    private var adapter: FindAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        adapter = FindAdapter(ArrayList())
        val config = CardSetting()
//        mViewBind.userListView.layoutManager = CardLayoutManager(
//            ReItemTouchHelper(
//                CardTouchHelperCallback(
//                    mViewBind.userListView,
//                    adapter?.data!!,
//                    config
//                )
//            ), config
//        )
    }

    override fun lazyLoadData() {
        mViewModel.getFindUsers()
    }

    override fun createObserver() {
        mViewModel.findUserLiveData.observe(this) {

        }
    }

    override fun showLoading(message: String) {

    }

    override fun dismissLoading() {

    }
}