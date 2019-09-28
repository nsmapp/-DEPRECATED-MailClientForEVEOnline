package by.nepravskysm.database.entity.converter

import androidx.room.TypeConverter
import by.nepravskysm.database.entity.subentity.RecipientDBE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipientConverter {

    @TypeConverter

    fun toDBRecipient(recipients: List<RecipientDBE>):String{
        return Gson().toJson(recipients)
    }

    @TypeConverter
    fun fromListRecipients(recipients: String): List<RecipientDBE>{
        val list = object : TypeToken<List<RecipientDBE>>(){}.type
        return Gson().fromJson(recipients, list)
    }


}