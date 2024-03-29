package by.nepravskysm.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.nepravskysm.database.entity.MailHeaderDBE

@Dao
interface MailHeaderDao {

    companion object {
        const val TABLE_NAME = "mail_headers"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMailHeaders(headersList :List<MailHeaderDBE>)

    @Query("SELECT * FROM $TABLE_NAME WHERE ownerName = :ownerName AND labels != :labelMask ORDER BY id DESC LIMIT 100")
    fun getMailsHeaders(ownerName: String, labelMask: String): List<MailHeaderDBE>

    @Query("SELECT * FROM $TABLE_NAME WHERE ownerName = :ownerName AND id < :lastMailId AND labels LIKE :labelMask  ORDER BY id DESC LIMIT 50")
    fun getMailsHeadersWithLabels(
        ownerName: String,
        labelMask: String,
        lastMailId: Long
    ): List<MailHeaderDBE>

    @Query("SELECT * FROM $TABLE_NAME WHERE isRead = :readStatus AND ownerName = :ownerName")
    fun getUnreadMails(ownerName: String, readStatus: Boolean = false): List<MailHeaderDBE>

    @Query("SELECT MAX(id) FROM $TABLE_NAME WHERE ownerName = :characterName")
    fun getLastMailId(characterName:String):Long

    @Query("UPDATE $TABLE_NAME SET isRead = 1 WHERE id = :mailId ")
    fun setMailIsRead(mailId: Long)

    @Query("UPDATE $TABLE_NAME SET isRead = 1 WHERE isRead = 0 AND ownerName = :characterName")
    fun setAllMailAsRead(characterName: String)

    @Query("DELETE FROM $TABLE_NAME WHERE id = :mailId ")
    fun delelteMail(mailId: Long)


}