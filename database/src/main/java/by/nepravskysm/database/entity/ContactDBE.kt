package by.nepravskysm.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.nepravskysm.database.dao.CharacterContactsDAO
import com.google.gson.annotations.SerializedName

@Entity(tableName = CharacterContactsDAO.TABLE_NAME)
data class ContactDBE(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val contactId: Long,
    @SerializedName("contact_type")
    val contactType: String,
    @SerializedName("standing")
    val standing: Double,
    @SerializedName("contact_name")
    val contactName: String,
    @SerializedName("contact_owner")
    val owner: String
)
