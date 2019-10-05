package by.nepravskysm.domain.usecase.auth

import by.nepravskysm.domain.entity.CharacterInfo
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.rest.auth.CharacterInfoRepository
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase


class AuthUseCase(private val authRepository: AuthRepository,
                  private val authInfoRepository: AuthInfoRepository,
                  private val characterInfoRepository: CharacterInfoRepository,
                  private val activeCharacterRepository: ActiveCharacterRepository
) : AsyncUseCase<CharacterInfo>() {

    private var firstAuthToken: String = ""

    fun setData(token :String){
        firstAuthToken = token
    }


    override suspend fun onBackground(): CharacterInfo {

        val authToken = authRepository.getAuthToken(firstAuthToken)
        val characterInfo = characterInfoRepository.getCharacterInfo(authToken.accessToken)

        authInfoRepository.saveAuthInfo(authToken.accessToken,
            authToken.refreshToken,
            characterInfo.characterID,
            characterInfo.characterName)
        activeCharacterRepository.insertCharacterName(characterInfo.characterName)
        return characterInfo
    }


}