package by.nepravskysm.domain.entity

data class CharacterInfo(

    val characterID :Long,

    val characterName :String,

    val characterOwnerHash :String,

    val expiresOn :String,

//    val intellectualProperty :String,

    val scopes :String,

    val tokenType :String

)