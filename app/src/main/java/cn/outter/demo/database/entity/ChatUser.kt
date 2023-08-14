package cn.outter.demo.database.entity

import com.google.gson.Gson

data class ChatUser(
    var userId:String,
    var userName: String,
    var avatarUrl: String
):java.io.Serializable {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}