package by.nepravskysm.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.nepravskysm.database.entity.ContactDBE

@Dao
interface CharacterContactsDAO {

    companion object {
        const val TABLE_NAME = "contacts"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContacts(contactList: List<ContactDBE>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contact: ContactDBE)

    @Query("SELECT * FROM $TABLE_NAME WHERE standing > :minStanding AND owner = :characterName ORDER BY contactName")
    fun getContactList(characterName: String , minStanding: Double): List<ContactDBE>
}