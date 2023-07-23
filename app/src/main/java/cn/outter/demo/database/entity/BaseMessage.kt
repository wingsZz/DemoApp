package cn.outter.demo.database.entity

import com.google.gson.Gson
import java.io.Serializable

open class BaseMessage:Serializable {
    var msgType:Int = -1

    override fun toString(): String {
        return Gson().toJson(this)
    }
}