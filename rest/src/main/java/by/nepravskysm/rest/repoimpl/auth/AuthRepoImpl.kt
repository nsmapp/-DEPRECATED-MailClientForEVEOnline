package by.nepravskysm.rest.repoimpl.auth

import by.nepravskysm.domain.entity.Token
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.rest.api.AuthManager

class AuthRepoImpl(private val authManager: AuthManager) :
    AuthRepository {


    override suspend fun getAuthToken(code: String): Token {
        val authInfoRE =  authManager.getAuthToken(code).await()
        return Token(
                authInfoRE.accessToken,
                authInfoRE.refreshToken)
    }

    override suspend fun refreshAuthToken(refreshToken: String): Token {
        val refreshTokenRE = authManager.refreshToken(refreshToken).await()
        return Token(
            refreshTokenRE.accessToken,
            refreshTokenRE.refreshToken)
    }


}