package by.nepravskysm.domain.entity

data class AuthInfo (

    val accessToken: String,
    val refreshToken: String,
    val characterId :Long,
    val characterName :String)