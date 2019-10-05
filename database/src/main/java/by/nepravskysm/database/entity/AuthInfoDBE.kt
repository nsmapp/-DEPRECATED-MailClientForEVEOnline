package by.nepravskysm.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import by.nepravskysm.database.dao.AuthInfoDAO
import com.google.gson.annotations.SerializedName

@Entity(tableName = AuthInfoDAO.TABLE_NAME,
    indices = [Index(value = ["characterName", "characterId"], unique = true)]
)
data class AuthInfoDBE     (



    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("character_id")

    val characterId :Long,

    @SerializedName("character_name")
    val characterName :String
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}