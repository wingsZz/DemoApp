package cn.outter.demo.conversation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cn.outter.demo.DataCacheInMemory
import cn.outter.demo.database.entity.ImgMsg
import cn.outter.demo.database.entity.Message
import cn.outter.demo.database.entity.MessageContentUtil
import cn.outter.demo.database.entity.MsgType
import cn.outter.demo.database.entity.TxtMsg
import cn.outter.demo.databinding.OutterItemPicMsgFromMeBinding
import cn.outter.demo.databinding.OutterItemPicMsgToMeBinding
import cn.outter.demo.databinding.OutterItemTxtMsgFromMeBinding
import cn.outter.demo.databinding.OutterItemTxtMsgToMeBinding
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemAdapter

class MessageAdapter(): BaseMultiItemAdapter<Message>() {
    private val mine = DataCacheInMemory.mine
    init {
        addItemType(MessageItemType.MESSAGE_TXT_FROM_ME, object : OnMultiItemAdapterListener<Message, TextMessageFromMeViewHolder> {
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): TextMessageFromMeViewHolder {
                val viewBinding =
                    OutterItemTxtMsgFromMeBinding.inflate(LayoutInflater.from(context), parent, false)
                return TextMessageFromMeViewHolder(viewBinding)
            }

            override fun onBind(holder: TextMessageFromMeViewHolder, position: Int, item: Message?) {
                if (item == null) return
                val txtMsg = MessageContentUtil.getMsgContent<TxtMsg>(item)
                if (txtMsg == null) {
                    return
                }
                holder.viewBinding.msgContent.text = txtMsg.content + item.sendTime
//                holder.viewBinding.icon.setImageResource(item.imageResource)
            }
        }).addItemType(MessageItemType.MESSAGE_PIC_FROM_ME, object : OnMultiItemAdapterListener<Message, PicMessageFromMeViewHolder> {
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): PicMessageFromMeViewHolder {
                val viewBinding =
                    OutterItemPicMsgFromMeBinding.inflate(LayoutInflater.from(context), parent, false)
                return PicMessageFromMeViewHolder(viewBinding)
            }

            override fun onBind(holder: PicMessageFromMeViewHolder, position: Int, item: Message?) {
                if (item == null) return

                val imgMsg = MessageContentUtil.getMsgContent<ImgMsg>(item)
                if (imgMsg == null) {
                    return
                }
                Glide.with(holder.viewBinding.content).load(imgMsg.imageLocalPath).circleCrop().into(holder.viewBinding.content)
            }

            override fun isFullSpanItem(itemType: Int): Boolean {
                return true
            }

        }).addItemType(MessageItemType.MESSAGE_TXT_TO_ME, object : OnMultiItemAdapterListener<Message, TextMessageToMeViewHolder> {
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): TextMessageToMeViewHolder {
                val viewBinding =
                    OutterItemTxtMsgToMeBinding.inflate(LayoutInflater.from(context), parent, false)
                return TextMessageToMeViewHolder(viewBinding)
            }

            override fun onBind(holder: TextMessageToMeViewHolder, position: Int, item: Message?) {
                if (item == null) return
                val txtMsg = MessageContentUtil.getMsgContent<TxtMsg>(item)
                if (txtMsg == null) {
                    return
                }
                holder.viewBinding.msgContent.text = txtMsg.content + item.sendTime
            }

            override fun isFullSpanItem(itemType: Int): Boolean {
                return true
            }

        }).addItemType(MessageItemType.MESSAGE_PIC_TO_ME, object : OnMultiItemAdapterListener<Message, PicMessageToMeViewHolder> {
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): PicMessageToMeViewHolder {
                val viewBinding =
                    OutterItemPicMsgToMeBinding.inflate(LayoutInflater.from(context), parent, false)
                return PicMessageToMeViewHolder(viewBinding)
            }

            override fun onBind(holder: PicMessageToMeViewHolder, position: Int, item: Message?) {
                if (item == null) return
                val imgMsg = MessageContentUtil.getMsgContent<ImgMsg>(item)
                if (imgMsg == null) {
                    return
                }
                Glide.with(holder.viewBinding.content).load(imgMsg.imageLocalPath).circleCrop().into(holder.viewBinding.content)
            }

            override fun isFullSpanItem(itemType: Int): Boolean {
                return true
            }

        }).onItemViewType { position, list ->
            if (list[position].fromId == (mine?.id ?: "")) {
                if (list[position].msgType == MsgType.TXT) {
                    return@onItemViewType MessageItemType.MESSAGE_TXT_FROM_ME
                } else if (list[position].msgType == MsgType.PIC) {
                    return@onItemViewType MessageItemType.MESSAGE_PIC_FROM_ME
                }
            } else {
                if (list[position].msgType == MsgType.TXT) {
                    return@onItemViewType MessageItemType.MESSAGE_TXT_TO_ME
                } else if (list[position].msgType == MsgType.PIC) {
                    return@onItemViewType MessageItemType.MESSAGE_PIC_TO_ME
                }
            }
            MessageItemType.MESSAGE_TXT_FROM_ME
        }
    }



    class MessageViewHolder(itemView: View):ViewHolder(itemView) {

    }

    class TextMessageFromMeViewHolder(val viewBinding: OutterItemTxtMsgFromMeBinding):ViewHolder(viewBinding.root) {

    }

    class TextMessageToMeViewHolder(val viewBinding: OutterItemTxtMsgToMeBinding):ViewHolder(viewBinding.root) {

    }

    class PicMessageFromMeViewHolder(val viewBinding: OutterItemPicMsgFromMeBinding):ViewHolder(viewBinding.root) {

    }

    class PicMessageToMeViewHolder(val viewBinding: OutterItemPicMsgToMeBinding):ViewHolder(viewBinding.root) {

    }
}