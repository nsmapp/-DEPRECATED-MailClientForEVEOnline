package by.nepravskysm.domain.usecase.auth

import by.nepravskysm.domain.entity.Token
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase


class RefreshAuthTokenUseCase(
    private val authRepo: AuthRepository,
    private val authInfoRepo: AuthInfoRepository,
    private val activeCharacterRepo: ActiveCharacterRepository
)    : AsyncUseCase<Token>() {


    private var token: String =""

    fun setData(authToken: String): RefreshAuthTokenUseCase {
        token = authToken
        return this
    }

    override suspend fun onBackground(): Token {
        val characterName = activeCharacterRepo.getActiveCharacterName()
        val authInfo = authRepo.refreshAuthToken(token)
        authInfoRepo.saveNewToken(
            authInfo.accessToken,
            authInfo.refreshToken,
            characterName)

        return authInfo
    }


}