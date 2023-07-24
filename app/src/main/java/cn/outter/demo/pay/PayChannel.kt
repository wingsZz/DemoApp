package cn.outter.demo.pay

@Retention(AnnotationRetention.SOURCE)
annotation class PayChannel {
    companion object {
        val ALI_PAY = 1

        val WECHAT_PAY = 2
    }
}
