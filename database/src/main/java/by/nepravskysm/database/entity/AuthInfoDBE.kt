package by.nepravskysm.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.nepravskysm.database.dao.AuthInfoDAO
import com.google.gson.annotations.SerializedName

@Entity(tableName = AuthInfoDAO.TABLE_NAME)
data class AuthInfoDBE     (

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("character_id")
    val characterId :Long,

    @SerializedName("character_name")
    val characterName :String
)