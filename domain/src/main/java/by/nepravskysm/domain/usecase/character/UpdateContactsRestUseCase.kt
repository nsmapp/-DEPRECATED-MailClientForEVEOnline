package by.nepravskysm.domain.usecase.character

import by.nepravskysm.domain.entity.AuthInfo
import by.nepravskysm.domain.entity.Contact
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.database.DBCharacterContactsRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.character.CharacterContactsRepository
import by.nepravskysm.domain.repository.utils.NamesRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class UpdateContactsRestUseCase(
    private val authRepo: AuthRepository,
    private val authInfoRepo: AuthInfoRepository,
    private val activeCharacterRepo: ActiveCharacterRepository,
    private val characterContactsRepo: CharacterContactsRepository,
    private val dbCharacterContactsRepo: DBCharacterContactsRepository,
    private val namesRepo: NamesRepository
) : AsyncUseCase<Boolean>() {


    private var characterId: Long = 0

    fun setData(characterId: Long): UpdateContactsRestUseCase{
        this.characterId = characterId
        return this
    }
    override suspend fun onBackground(): Boolean {
        val characterName = activeCharacterRepo.getActiveCharacterName()
        val authInfo: AuthInfo = authInfoRepo.getAuthInfo(characterName)
        var contactsList = listOf<Contact>()
        try {
            contactsList = getContacts(authInfo.accessToken, authInfo.characterId)
        } catch (e: Exception) {
            val token = authRepo.refreshAuthToken(authInfo.refreshToken)
            contactsList = getContacts(token.accessToken, authInfo.characterId)

        }
        setNameToContacts(contactsList)

        return saveContacts(contactsList, characterName)
    }


    private suspend fun getContacts(accessToken: String,
                                    characterId: Long
    ): List<Contact>{
        return characterContactsRepo.getContacts(characterId, accessToken)
    }

    private suspend fun saveContacts(contactList: List<Contact>, activaCharacter: String): Boolean{
        return dbCharacterContactsRepo.insertContactList(contactList, activaCharacter)
    }

    private suspend fun setNameToContacts (contactList: List<Contact>): List<Contact>{

        val idSet = contactList
            .map { it.contactId }
            .distinct()

        val nameMap: HashMap<Long, String> = namesRepo.getNameMap(idSet)
        for (contact in contactList) {
            contact.contactName = nameMap[contact.contactId] ?: "Name not found"
        }

        return contactList
    }

}