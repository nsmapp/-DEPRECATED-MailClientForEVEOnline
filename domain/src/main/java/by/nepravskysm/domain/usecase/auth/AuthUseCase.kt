package by.nepravskysm.domain.usecase.auth

import by.nepravskysm.domain.entity.CharacterInfo
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.auth.CharacterInfoRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase


class AuthUseCase(
    private val authRepo: AuthRepository,
    private val authInfoRepo: AuthInfoRepository,
    private val characterInfoRepo: CharacterInfoRepository,
    private val activeCharacterRepo: ActiveCharacterRepository
) : AsyncUseCase<CharacterInfo>() {

    private var firstAuthToken: String = ""

    fun setData(token: String): AuthUseCase {
        firstAuthToken = token
        return this
    }

    override suspend fun onBackground(): CharacterInfo {

        val authToken = authRepo.getAuthToken(firstAuthToken)
        val characterInfo = characterInfoRepo.getCharacterInfo(authToken.accessToken)

        authInfoRepo.saveAuthInfo(
            authToken.accessToken,
            authToken.refreshToken,
            characterInfo.characterID,
            characterInfo.characterName)
        activeCharacterRepo.insertCharacterName(characterInfo.characterName)
        return characterInfo
    }


}