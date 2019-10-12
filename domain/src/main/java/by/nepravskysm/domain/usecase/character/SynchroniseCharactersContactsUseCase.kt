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
import java.lang.Exception
import java.util.logging.Level

class SynchroniseCharactersContactsUseCase(private val authRepository: AuthRepository,
                                           private val authInfoRepository: AuthInfoRepository,
                                           private val activeCharacterRepository: ActiveCharacterRepository,
                                           private val characterContactsRepository: CharacterContactsRepository,
                                           private val dbCharacterContactsRepository: DBCharacterContactsRepository,
                                           private val namesRepository: NamesRepository
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


            contactsList = getContacts(characterInfo.accessToken, characterInfo.characterId)
            setNameToContacts(contactsList)
        return saveContacts(contactsList, characterName)
    }


    private suspend fun getContacts(accessToken: String,
                                    characterId: Long
    ): List<Contact>{
        return characterContactsRepository.getContacts(characterId, accessToken)
    }

    private suspend fun saveContacts(contactList: List<Contact>, activaCharacter: String): Boolean{

        return  dbCharacterContactsRepository.insertContactList(contactList, activaCharacter)
    }

    private suspend fun setNameToContacts (contactList: List<Contact>): List<Contact>{

        val contacts = contactList.toMutableList()
        val idSet = mutableSetOf<Long>()

        for(contact in contactList){
            idSet.add(contact.contactId)
        }
        java.util.logging.Logger.getLogger("logd 00").log(Level.INFO, "${idSet.size}")
        val nameMap: HashMap<Long, String> = namesRepository.getNameMap(idSet.toList())


        java.util.logging.Logger.getLogger("logd 00").log(Level.INFO, "${nameMap}")


        for( contact in contacts){
            contact.contactName = nameMap[contact.contactId].toString()
        }


        java.util.logging.Logger.getLogger("logd 00").log(Level.INFO, "${contactList.size}")

        return contacts;
    }

}