package cn.outter.demo.database.entity

import com.google.gson.Gson

data class UserInfo(
    var userId:String?,
    var userName: String?,
    var avatarUrl: String?
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}