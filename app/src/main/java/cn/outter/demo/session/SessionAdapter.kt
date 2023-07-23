package cn.outter.demo.session

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.outter.demo.R
import cn.outter.demo.database.entity.Session
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class SessionAdapter:BaseQuickAdapter<Session,SessionAdapter.SessionViewHolder>(R.layout.item_session) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        return SessionViewHolder(View.inflate(context,R.layout.item_session,null))
    }

    override fun convert(holder: SessionViewHolder, item: Session) {
        holder.getView<TextView>(R.id.userLabel).text = item.userInfo.userName
    }

    class SessionViewHolder(itemView:View):BaseViewHolder(itemView) {

    }

}