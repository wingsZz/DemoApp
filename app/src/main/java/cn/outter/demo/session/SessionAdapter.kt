package cn.outter.demo.session

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cn.outter.demo.database.entity.Session
import cn.outter.demo.databinding.ItemSessionBinding
import com.chad.library.adapter.base.BaseQuickAdapter

class SessionAdapter : BaseQuickAdapter<Session, SessionAdapter.SessionViewHolder>() {


    override fun onBindViewHolder(holder: SessionViewHolder, position: Int, item: Session?) {
        if (item == null) {
            return
        }
        holder.viewBinding.userLabel.text = item.chatUser.userName
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): SessionViewHolder {
        return SessionViewHolder(ItemSessionBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    class SessionViewHolder(val viewBinding: ItemSessionBinding) : ViewHolder(viewBinding.root) {

    }


}