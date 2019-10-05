package by.nepravskysm.domain.usecase.auth

import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.entity.Token
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase


class RefreshAuthTokenUseCase(private val authRepository: AuthRepository,
                              private val authInfoRepository: AuthInfoRepository,
                              private val activeCharacterRepository: ActiveCharacterRepository
)    : AsyncUseCase<Token>() {


    private var token: String =""

    fun setData(authToken: String){
        token = authToken
    }

    override suspend fun onBackground(): Token {
        val characterName = activeCharacterRepository.getActiveCharacterName()
        val  authInfo = authRepository.refreshAuthToken(token)
        authInfoRepository.saveNewToken(authInfo.accessToken,
            authInfo.refreshToken,
            characterName)

        return authInfo
    }


}