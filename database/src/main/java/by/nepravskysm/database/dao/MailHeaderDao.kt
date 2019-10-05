package by.nepravskysm.database.dao

import androidx.room.*
import by.nepravskysm.database.entity.MailHeaderDBE

@Dao
interface MailHeaderDao {

    companion object {
        const val TABLE_NAME = "mail_headers"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMailHeaders(headersList :List<MailHeaderDBE>)

    @Query("SELECT * FROM $TABLE_NAME WHERE ownerName = :ownerName ORDER BY id DESC"  )
    fun getMailsHeaders(ownerName: String): List<MailHeaderDBE>

    @Query("SELECT MAX(id) FROM $TABLE_NAME WHERE ownerName = :characterName")
    fun getLastMailId(characterName:String):Long

    @Query("UPDATE $TABLE_NAME SET isRead = 1 WHERE id = :mailId ")
    fun setMailIsRead(mailId: Long)


}