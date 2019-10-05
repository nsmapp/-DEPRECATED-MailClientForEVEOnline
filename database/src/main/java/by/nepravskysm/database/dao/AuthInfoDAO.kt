package by.nepravskysm.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.nepravskysm.database.entity.AuthInfoDBE

@Dao
interface AuthInfoDAO {

    companion object {

        const val TABLE_NAME = "auth_info"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAuthInfo(authInfoDBE: AuthInfoDBE)

    @Query("UPDATE $TABLE_NAME SET refreshToken = :refreshToken, accessToken = :accessToken WHERE characterName = :characterName")
    fun updateToken(accessToken :String, refreshToken :String, characterName: String)

    @Query("SELECT * FROM $TABLE_NAME WHERE characterName = :characterName")
    fun getAuthInfo(characterName: String) : AuthInfoDBE

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllCharactersAuthInfo() : List<AuthInfoDBE>
}