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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertContacts(contactList: List<ContactDBE>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertContact(contact: ContactDBE)

    @Query("SELECT * FROM $TABLE_NAME WHERE standing > :minStanding  ORDER BY contactName")
    fun getContactList( minStanding: Double): List<ContactDBE>
}