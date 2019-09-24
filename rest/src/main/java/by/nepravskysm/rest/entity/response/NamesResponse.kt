package by.nepravskysm.rest.entity.response


import com.google.gson.annotations.SerializedName

data class NamesResponse(
    @SerializedName("category")
    val category: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String
)