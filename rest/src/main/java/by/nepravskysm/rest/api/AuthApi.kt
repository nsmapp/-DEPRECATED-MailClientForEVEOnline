package by.nepravskysm.rest.api

import by.nepravskysm.rest.entity.response.AuthTokenResponse
import by.nepravskysm.rest.entity.response.RefreshTokenResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface AuthApi{

    @FormUrlEncoded
    @POST("/v2/oauth/token")
    fun getAuthInfo(
        @Header("Authorization") authHeader :String,
        @Field("grant_type") grantType :String,
        @Field("code") code :String
    ) : Deferred<AuthTokenResponse>

    @FormUrlEncoded
    @POST("/v2/oauth/token")
    fun getRefreshToken(
        @Field("grant_type") grantType :String,
        @Field("refresh_token") refreshToken :String,
        @Field("client_id") clientId :String
    ) : Deferred<RefreshTokenResponse>


}