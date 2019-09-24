package by.nepravskysm.rest.entity.response

import com.google.gson.annotations.SerializedName

data class CharacterInfoResponse (

    @SerializedName("CharacterID")
    val characterID :Long,

    @SerializedName("CharacterName")
    val characterName :String,

    @SerializedName("CharacterOwnerHash")
    val characterOwnerHash :String,

    @SerializedName("ExpiresOn")
    val expiresOn :String,

    @SerializedName("IntellectualProperty")
    val intellectualProperty :String,

    @SerializedName("Scopes")
    val scopes :String,

    @SerializedName("TokenType")
    val tokenType :String

)