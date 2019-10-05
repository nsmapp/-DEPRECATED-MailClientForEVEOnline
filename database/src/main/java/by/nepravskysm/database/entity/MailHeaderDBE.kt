package by.nepravskysm.database.entity

import androidx.room.*
import by.nepravskysm.database.dao.MailHeaderDao
import by.nepravskysm.database.entity.converter.LabelConverter
import by.nepravskysm.database.entity.converter.RecipientConverter
import by.nepravskysm.database.entity.subentity.RecipientDBE
import com.google.gson.annotations.SerializedName

@Entity(tableName = MailHeaderDao.TABLE_NAME)
data class MailHeaderDBE (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val mailId: Long,

    @SerializedName("from_id")
    val fromId: Long,

    @SerializedName("from_nameame")
    val fromName:String,

    @SerializedName("is_read")
    val isRead: Boolean,

    @SerializedName("labels")
    @TypeConverters(LabelConverter::class)
    val labels: List<Int>,

    @SerializedName("recipients")
    @TypeConverters(RecipientConverter::class)
    val recipients: List<RecipientDBE>,

    @SerializedName("subject")
    val subject: String,

    @SerializedName("timestamp")
    val timestamp: String,

    @SerializedName("owner")
    val ownerName: String

)