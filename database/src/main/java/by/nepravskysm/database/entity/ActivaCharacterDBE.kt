package by.nepravskysm.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.nepravskysm.database.dao.ActiveCharacterDao
import com.google.gson.annotations.SerializedName

@Entity(tableName = ActiveCharacterDao.TABLE_NAME)
data class ActivaCharacterDBE(
    @SerializedName("character_name")
    val characterName: String
) {
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id = 0
}