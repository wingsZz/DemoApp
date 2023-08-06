package cn.outter.demo.bean

data class User(
    var id:Long,
    var username:String = "",
    var token:String? = ""
)
