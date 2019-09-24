package by.nepravskysm.domain.repository.rest.auth

import by.nepravskysm.domain.entity.Token

interface AuthRepository {

    suspend fun getAuthToken(code :String) :Token
    suspend fun refreshAuthToken(refreshToken :String) : Token
}