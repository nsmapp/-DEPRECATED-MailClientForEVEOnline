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

    @Query("SELECT * FROM $TABLE_NAME ORDER BY id DESC" )
    fun getMailsHeaders(): List<MailHeaderDBE>

    @Query("SELECT MAX(id) FROM $TABLE_NAME")
    fun getLastMailId():Long


}