package cn.outter.demo.conversation

import android.util.SparseArray
import android.util.SparseIntArray
import android.view.View
import cn.outter.demo.R
import cn.outter.demo.database.entity.Message
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class MessageAdapter(): BaseDelegateMultiAdapter<Message, MessageAdapter.MessageViewHolder>() {

    init {
        val layoutMap = SparseIntArray()
        layoutMap.put(MessageItemType.MESSAGE_TXT_FROM_ME, R.layout.outter_item_txt_message_from_me)
        layoutMap.put(MessageItemType.MESSAGE_TXT_FROM_ME, R.layout.outter_item_txt_message_from_me)
        layoutMap.put(MessageItemType.MESSAGE_TXT_FROM_ME, R.layout.outter_item_txt_message_from_me)
        layoutMap.put(MessageItemType.MESSAGE_TXT_FROM_ME, R.layout.outter_item_txt_message_from_me)
        setMultiTypeDelegate(MessageTypeDelegate(layoutMap))
    }

    override fun convert(holder: MessageViewHolder, item: Message) {

    }


    class MessageViewHolder(itemView: View):BaseViewHolder(itemView) {

    }
}