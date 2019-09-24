package by.nepravskysm.rest.entity.subentity


import com.google.gson.annotations.SerializedName

data class Recipient(
    @SerializedName("recipient_id")
    val recipientId: Long,
    @SerializedName("recipient_type")
    val recipientType: String
)