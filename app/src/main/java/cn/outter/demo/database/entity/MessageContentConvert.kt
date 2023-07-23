package cn.outter.demo.database.entity

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class MessageContentConvert {
    @TypeConverter
    fun fromStringToMessageContent(userInfoJson: String): Map<String,Any>? {
        return Gson().fromJson(userInfoJson, object : TypeToken<Map<String, Any>>() {}.type)
    }

    @TypeConverter
    fun fromMessageContentToString(messageContent: Map<String,Any>?): String? {
        return if(messageContent == null) "" else Gson().toJson(messageContent)
    }
}