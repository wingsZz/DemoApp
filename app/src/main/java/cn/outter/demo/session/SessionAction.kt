package cn.outter.demo.session

import cn.outter.demo.database.entity.Session
import com.jeremyliao.liveeventbus.core.LiveEvent

data class SessionAction(
    var type:Int,
    var session:Session
):LiveEvent {

}