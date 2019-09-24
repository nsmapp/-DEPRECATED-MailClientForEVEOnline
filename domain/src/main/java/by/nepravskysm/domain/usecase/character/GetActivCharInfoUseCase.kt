package by.nepravskysm.domain.usecase.character

import by.nepravskysm.domain.entity.AuthInfo
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class GetActivCharInfoUseCase(private val authInfoRepository: AuthInfoRepository) : AsyncUseCase<AuthInfo>(){


    override suspend fun onBackground(): AuthInfo {
       return authInfoRepository.getAuthInfo()
    }
}