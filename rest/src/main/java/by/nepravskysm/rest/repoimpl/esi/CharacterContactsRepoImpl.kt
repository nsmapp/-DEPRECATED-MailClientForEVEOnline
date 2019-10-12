package by.nepravskysm.rest.repoimpl.esi

import by.nepravskysm.domain.entity.Contact
import by.nepravskysm.domain.repository.rest.character.CharacterContactsRepository
import by.nepravskysm.rest.api.EsiManager

class CharacterContactsRepoImpl(private val esiManager: EsiManager): CharacterContactsRepository{

    override suspend fun getContacts(characterId: Long, accessToken: String): List<Contact> {
        val contactList = esiManager.getContactList(accessToken, characterId).await()
        val domainContactsList = mutableListOf<Contact>()

        for(contct in contactList){
            if (contct.contactType.equals("character")){
                val domainContact = Contact(contct.contactId,
                    contct.contactType,
                    contct.standing)
                domainContactsList.add(domainContact)
            }
        }

        return  domainContactsList
    }
}
