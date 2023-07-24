package cn.outter.demo.conversation

import android.util.SparseIntArray
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.database.entity.Message
import cn.outter.demo.database.entity.MsgType
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate

class MessageTypeDelegate(layouts:SparseIntArray): BaseMultiTypeDelegate<Message>(layouts) {
    private val mine = DataCacheInMemory.mine

    override fun getItemType(data: List<Message>, position: Int): Int {
        if (data[position].fromId == (mine?.id ?: "")) {
            if (data[position].msgType == MsgType.TXT) {
                return MessageItemType.MESSAGE_TXT_FROM_ME
            } else if (data[position].msgType == MsgType.PIC) {
                return MessageItemType.MESSAGE_PIC_FROM_ME
            }
        } else {
            if (data[position].msgType == MsgType.TXT) {
                return MessageItemType.MESSAGE_TXT_TO_ME
            } else if (data[position].msgType == MsgType.PIC) {
                return MessageItemType.MESSAGE_PIC_TO_ME
            }
        }

        return 0
    }
}