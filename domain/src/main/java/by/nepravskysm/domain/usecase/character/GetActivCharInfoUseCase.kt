package by.nepravskysm.domain.usecase.character

import by.nepravskysm.domain.entity.AuthInfo
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class GetActivCharInfoUseCase(private val authInfoRepository: AuthInfoRepository,
                              private val activeCharacterRepository: ActiveCharacterRepository) : AsyncUseCase<AuthInfo>(){


    override suspend fun onBackground(): AuthInfo {
        val characnerName = activeCharacterRepository.getActiveCharacterName()
       return authInfoRepository.getAuthInfo(characnerName)
    }
}