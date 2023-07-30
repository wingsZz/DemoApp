package cn.outter.demo.database.entity

import com.google.gson.Gson

object MessageContentUtil {
    inline fun <reified T> getMsgContent(message: Message):T? {
        if (message.msgContent == null) {
            message.msgContent = Gson().fromJson(message.map?.get("content") as String,T::class.java)
        }
        return message.msgContent as T?
    }
}