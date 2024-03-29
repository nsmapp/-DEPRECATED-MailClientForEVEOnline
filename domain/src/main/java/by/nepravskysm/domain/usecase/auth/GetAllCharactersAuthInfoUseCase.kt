package by.nepravskysm.domain.usecase.auth

import by.nepravskysm.domain.entity.AuthInfo
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class GetAllCharactersAuthInfoUseCase(private val authInfoRepo: AuthInfoRepository)
    : AsyncUseCase<List<AuthInfo>>(){

    override suspend fun onBackground(): List<AuthInfo> = authInfoRepo.getAllCharacters()
}