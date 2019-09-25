package by.nepravskysm.rest.entity.request


import com.google.gson.annotations.SerializedName

data class MailMetadataRequest(
    @SerializedName("labels")
    val labels: List<Int>,
    @SerializedName("read")
    val read: Boolean
)