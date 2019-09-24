package by.nepravskysm.rest.entity.response


import by.nepravskysm.rest.entity.subentity.Recipient
import com.google.gson.annotations.SerializedName

data class MailHeaderResponse(
    @SerializedName("from")
    val from: Long,
    @SerializedName("is_read")
    val isRead: Boolean,
    @SerializedName("labels")
    val labels: List<Int>,
    @SerializedName("mail_id")
    val mailId: Long,
    @SerializedName("recipients")
    val recipientRespons: List<Recipient>,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("timestamp")
    val timestamp: String
)

