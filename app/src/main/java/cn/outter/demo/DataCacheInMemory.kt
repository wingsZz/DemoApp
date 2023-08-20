package cn.outter.demo

import cn.outter.demo.bean.User
import com.google.gson.Gson
import com.tencent.mmkv.MMKV

object DataCacheInMemory {
    val MINE_KEY = "otter_user_mine"

    var mine: User? = null

    fun refreshMine(user: User?) {
        mine = user
        if (mine == null) {
            MMKV.defaultMMKV().putString(MINE_KEY, "")
        } else {
            MMKV.defaultMMKV().putString(MINE_KEY, Gson().toJson(mine))
        }
    }
}
