package by.nepravskysm.domain.usecase.character

import by.nepravskysm.domain.entity.AuthInfo
import by.nepravskysm.domain.entity.Contact
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.database.DBCharacterContactsRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.character.CharacterContactsRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase
import java.lang.Exception

class SynchroniseCharactersContactsUseCase(private val authRepository: AuthRepository,
                                           private val authInfoRepository: AuthInfoRepository,
                                           private val activeCharacterRepository: ActiveCharacterRepository,
                                           private val characterContactsRepository: CharacterContactsRepository,
                                           private val dbCharacterContactsRepository: DBCharacterContactsRepository
) : AsyncUseCase<Boolean>() {


    private var characterId: Long = 0

    fun setData(characterId: Long): SynchroniseCharactersContactsUseCase{
        this.characterId = characterId
        return this
    }
    override suspend fun onBackground(): Boolean {
        val characterName = activeCharacterRepository.getActiveCharacterName()
        val characterInfo: AuthInfo = authInfoRepository.getAuthInfo(characterName)

        var contactsList = listOf<Contact>()

        return try {
            contactsList = getContacts(characterInfo.accessToken, characterInfo.characterId)
            saveContacts(contactsList, characterName)

        }catch (e: Exception){
            val token = authRepository.refreshAuthToken(characterInfo.refreshToken).accessToken
            contactsList = getContacts(token, characterInfo.characterId)
            saveContacts(contactsList, characterName)

        }
    }


    private suspend fun getContacts(accessToken: String,
                                    characterId: Long
    ): List<Contact>{
        return characterContactsRepository.getContacts(characterId, accessToken)
    }

    private suspend fun saveContacts(contactList: List<Contact>, activaCharacter: String): Boolean{

        return  dbCharacterContactsRepository.insertContactList(contactList, activaCharacter)
    }
}