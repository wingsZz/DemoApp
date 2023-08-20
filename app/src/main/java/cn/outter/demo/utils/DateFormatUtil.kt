package cn.outter.demo.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormatUtil {
    fun formatTimeString(time: Long, dstPatten: String?): String {
        var localDstPatten = dstPatten
        if (dstPatten == null || dstPatten.trim { it <= ' ' } == "") localDstPatten = "yy-MM-dd HH:mm"
        val ret: String
        val date = Date(time)
        val sdf = SimpleDateFormat(localDstPatten, Locale.getDefault())
        ret = sdf.format(date)
        return ret
    }

    fun formatTimeStringByZone(time: Long, timeZone: String?, dstPatten: String?): String {
        var localDstPatten = dstPatten
        if (dstPatten == null || dstPatten.trim { it <= ' ' } == "") localDstPatten = "yy-MM-dd HH:mm"
        val ret: String
        val date = Date(time)
        val sdf = SimpleDateFormat(localDstPatten,Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone(timeZone)
        ret = sdf.format(date)
        return ret
    }
}