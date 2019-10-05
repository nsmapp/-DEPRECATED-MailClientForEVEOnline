package by.nepravskysm.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.nepravskysm.database.entity.ActivaCharacterDBE

@Dao
interface ActiveCharacterDao {

    companion object {
        const val TABLE_NAME = "active_character"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacterName(activeCharacter: ActivaCharacterDBE)

    @Query("UPDATE $TABLE_NAME SET characterName = :characterName WHERE id = 0")
    fun updateActiveCharacterName(characterName: String)

    @Query("SELECT characterName FROM $TABLE_NAME WHERE id = 0")
    fun getActiveCharacterName() : String
}