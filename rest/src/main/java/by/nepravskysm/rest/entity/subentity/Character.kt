package by.nepravskysm.rest.entity.subentity


import com.google.gson.annotations.SerializedName

data class Character(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String
)