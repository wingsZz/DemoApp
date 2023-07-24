package cn.outter.demo

import cn.outter.demo.bean.User
import com.google.gson.Gson
import com.tencent.mmkv.MMKV

object DataCacheInMemory {
    val MINE_KEY = "otter_user_mine"

    var mine: User? = Gson().fromJson(MMKV.defaultMMKV().getString("", ""), User::class.java)

    fun refreshMine(user: User?) {
        if (user == null) {
            return
        }
        mine = user
        MMKV.defaultMMKV().putString("", Gson().toJson(mine))
    }
}
