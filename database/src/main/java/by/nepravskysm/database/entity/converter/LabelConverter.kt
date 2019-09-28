package by.nepravskysm.database.entity.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class LabelConverter {

    @TypeConverter
    fun fromListInt(list :List<Int>):String{
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromString(value: String): List<Int>{
        val intList = object : TypeToken<List<Int>>(){}.type

        return Gson().fromJson(value, intList)
    }
}