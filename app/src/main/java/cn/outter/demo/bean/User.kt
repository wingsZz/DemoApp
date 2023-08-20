package cn.outter.demo.bean

import java.io.Serializable

data class User(
    var id:String? = "",
    var username:String? = "",
    var token:String = "",
    var nickname:String? = "",
    var gender:Int?,
    var birthday:String?,
    var avatarUrl:String?,
    var photoUrl:String?
):Serializable
