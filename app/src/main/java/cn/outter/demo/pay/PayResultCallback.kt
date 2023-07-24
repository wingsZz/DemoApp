package cn.outter.demo.pay

interface PayResultCallback {
    fun onSuccess(message:String,channel:Int)

    fun onError(message:String)

    fun onCancel(message: String)
}