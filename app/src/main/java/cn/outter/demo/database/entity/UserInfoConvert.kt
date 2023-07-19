package cn.outter.demo.database.entity

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson

@ProvidedTypeConverter
class UserInfoConvert {
    @TypeConverter
    fun fromStringToUserInfo(userInfoJson: String): UserInfo? {
        return Gson().fromJson(userInfoJson, UserInfo::class.java)
    }

    @TypeConverter
    fun fromUserInfoToString(userInfo: UserInfo?): String? {
        return userInfo?.toString()
    }
}