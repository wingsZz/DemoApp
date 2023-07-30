package cn.outter.demo.find

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cn.outter.demo.R
import cn.outter.demo.bean.FindUser
import cn.outter.demo.databinding.OutterItemFindUserBinding
import com.chad.library.adapter.base.BaseQuickAdapter
class FindAdapter(list:MutableList<FindUser>):BaseQuickAdapter<FindUser,FindAdapter.FindUserViewHolder>(list) {


    override fun onBindViewHolder(holder: FindUserViewHolder, position: Int, item: FindUser?) {

    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): FindUserViewHolder {
        return FindUserViewHolder(OutterItemFindUserBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    class FindUserViewHolder(viewBinding:OutterItemFindUserBinding):ViewHolder(viewBinding.root) {

    }
}