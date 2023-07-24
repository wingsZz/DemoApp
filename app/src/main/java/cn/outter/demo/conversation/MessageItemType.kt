package cn.outter.demo.conversation

@Retention(AnnotationRetention.SOURCE)
annotation class MessageItemType {
    companion object {
        val MESSAGE_TXT_TO_ME = 0
        val MESSAGE_TXT_FROM_ME = 1
        val MESSAGE_PIC_TO_ME = 2
        val MESSAGE_PIC_FROM_ME = 3
    }

}
