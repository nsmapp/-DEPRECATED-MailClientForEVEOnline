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

    @Query("UPDATE $TABLE_NAME SET refreshToken = :refreshToken, accessToken = :accessToken WHERE id = 0")

    fun updateToken(accessToken :String, refreshToken :String)

    @Query("SELECT * FROM $TABLE_NAME WHERE id = 0")
    fun getAuthInfo() : AuthInfoDBE
}