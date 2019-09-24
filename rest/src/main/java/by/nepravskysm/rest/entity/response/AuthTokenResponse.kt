package by.nepravskysm.rest.entity.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AuthTokenResponse (
    @SerializedName("access_token")
    @Expose
    val accessToken: String,

    @SerializedName("token_type")
    @Expose
    val tokenType: String,

    @SerializedName("expires_in")
    @Expose
    val expiresIn: Long,

    @SerializedName("refresh_token")
    @Expose
    val refreshToken: String)