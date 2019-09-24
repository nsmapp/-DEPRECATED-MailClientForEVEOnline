package by.nepravskysm.domain.repository.rest.auth
import by.nepravskysm.domain.entity.CharacterInfo

interface CharacterInfoRepository {

    suspend fun getCharacterInfo(accessToken :String) : CharacterInfo
}