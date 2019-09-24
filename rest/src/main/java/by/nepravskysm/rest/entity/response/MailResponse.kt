package by.nepravskysm.rest.entity.response


import com.google.gson.annotations.SerializedName

data class MailResponse(
    @SerializedName("body")
    val body: String,
    @SerializedName("from")
    val from: Long,
    @SerializedName("labels")
    val labels: List<Int>,
    @SerializedName("read")
    val read: Boolean,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("timestamp")
    val timestamp: String
)