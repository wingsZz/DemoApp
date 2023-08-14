package cn.outter.demo.database.entity

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson

@ProvidedTypeConverter
class UserInfoConvert {
    @TypeConverter
    fun fromStringToUserInfo(userInfoJson: String): ChatUser? {
        return Gson().fromJson(userInfoJson, ChatUser::class.java)
    }

    @TypeConverter
    fun fromUserInfoToString(chatUser: ChatUser?): String? {
        return chatUser?.toString()
    }
}