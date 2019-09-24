package by.nepravskysm.rest.entity.request


import by.nepravskysm.rest.entity.subentity.Recipient
import com.google.gson.annotations.SerializedName

data class MailRequest(
    @SerializedName("approved_cost")
    val approvedCost: Int = 0,
    @SerializedName("body")
    val body: String,
    @SerializedName("recipients")
    val recipients: List<Recipient>,
    @SerializedName("subject")
    val subject: String
)