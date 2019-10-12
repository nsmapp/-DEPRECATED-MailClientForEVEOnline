package by.nepravskysm.rest.entity.response


import com.google.gson.annotations.SerializedName

data class ContactsResponse(
    @SerializedName("contact_id")
    val contactId: Long,
    @SerializedName("contact_type")
    val contactType: String,
    @SerializedName("is_blocked")
    val isBlocked: Boolean,
    @SerializedName("is_watched")
    val isWatched: Boolean,
    @SerializedName("standing")
    val standing: Double
)