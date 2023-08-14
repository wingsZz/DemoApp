package cn.outter.demo.conversation

import cn.outter.demo.database.entity.ImgMsg
import cn.outter.demo.database.entity.Message
import cn.outter.demo.database.entity.MsgType
import cn.outter.demo.database.entity.TxtMsg

object MsgFactory {
    fun createTxtMsg(text: String, to: String, from: String):Message {
        val txtMsg = TxtMsg()
        txtMsg.msgType = MsgType.TXT
        txtMsg.content = text
        val map = mapOf<String, Any>(Pair("content", txtMsg.toString()))
        return Message("${to}_$from", map, MsgType.TXT, to, from, System.currentTimeMillis())
    }

    fun createPicMsg(localPath:String,imageUrl: String,width:Int,height:Int, to: String, from: String):Message {
        val imgMsg = ImgMsg()
        imgMsg.msgType = MsgType.PIC
        imgMsg.imageLocalPath = localPath
        imgMsg.imageUrl = imageUrl
        imgMsg.imageH = height
        imgMsg.imageW = width
        val map = mapOf<String, Any>(Pair("content", imgMsg.toString()))
        return Message("${to}_$from", map, MsgType.PIC, to, from, System.currentTimeMillis())
    }
}