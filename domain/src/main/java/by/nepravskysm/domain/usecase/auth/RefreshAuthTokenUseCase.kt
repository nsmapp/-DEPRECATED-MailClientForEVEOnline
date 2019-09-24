package by.nepravskysm.domain.usecase.auth

import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.entity.Token
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase


class RefreshAuthTokenUseCase(private val authRepository: AuthRepository,
                              private val authInfoRepository: AuthInfoRepository
)    : AsyncUseCase<Token>() {


    private var token: String =""

    fun setData(authToken: String){
        token = authToken
    }

    override suspend fun onBackground(): Token {
        val  authInfo = authRepository.refreshAuthToken(token)
        authInfoRepository.saveNewToken(authInfo.accessToken, authInfo.refreshToken)

        return authInfo
    }


}