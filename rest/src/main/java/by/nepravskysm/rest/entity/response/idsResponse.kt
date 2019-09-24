package by.nepravskysm.rest.entity.response



import by.nepravskysm.rest.entity.subentity.Alliance
import by.nepravskysm.rest.entity.subentity.Character
import by.nepravskysm.rest.entity.subentity.Corporation
import com.google.gson.annotations.SerializedName

data class idsResponse(
    @SerializedName("alliances")
    val alliances: List<Alliance>?,
    @SerializedName("characters")
    val characters: List<Character>?,
    @SerializedName("corporations")
    val corporations: List<Corporation>?
)