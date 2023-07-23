package cn.outter.demo.database.entity

@Retention(AnnotationRetention.SOURCE)
annotation class MsgType {
    companion object {
        var TXT = 1
        var PIC = 2
    }
}
