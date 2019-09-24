package by.nepravskysm.rest.repoimpl.esi

import by.nepravskysm.domain.repository.rest.auth.CharacterInfoRepository
import by.nepravskysm.domain.entity.CharacterInfo
import by.nepravskysm.rest.api.EsiManager

class CharacterInfoRepoImpl(private val esiManager: EsiManager) :
    CharacterInfoRepository {


    override suspend fun getCharacterInfo(accessToken: String): CharacterInfo {
        val charInfo =  esiManager.getCharecterInfo(accessToken).await()

        return CharacterInfo(charInfo.characterID,
                    charInfo.characterName,
                    charInfo.characterOwnerHash,
                    charInfo.expiresOn,
//                    charInfo.intellectualProperty,
                    charInfo.scopes,
                    charInfo.tokenType
        )
    }
}