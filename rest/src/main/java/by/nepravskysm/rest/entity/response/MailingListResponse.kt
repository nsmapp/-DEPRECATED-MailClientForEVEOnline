package by.nepravskysm.rest.entity.response


import com.google.gson.annotations.SerializedName

data class MailingListResponse(
    @SerializedName("mailing_list_id")
    val mailingListId: Long,
    @SerializedName("name")
    val name: String
)