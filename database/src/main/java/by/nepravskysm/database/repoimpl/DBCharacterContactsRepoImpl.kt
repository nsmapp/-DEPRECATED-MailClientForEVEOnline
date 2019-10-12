package by.nepravskysm.database.repoimpl

import by.nepravskysm.database.AppDatabase
import by.nepravskysm.database.entity.ContactDBE
import by.nepravskysm.domain.entity.Contact
import by.nepravskysm.domain.repository.database.DBCharacterContactsRepository

class DBCharacterContactsRepoImpl(private val appDatabase: AppDatabase) : DBCharacterContactsRepository {

    override suspend fun insertContactList(contactList: List<Contact>, characterName: String): Boolean {

        val contacts = mutableListOf<ContactDBE>()
        for (contact in contactList){
            contacts.add(createContactDBE(contact, characterName))
        }

        appDatabase.characterContactsDao().insertContacts(contacts)
        return true
    }

    override suspend fun insertContact(contact: Contact, characterName: String) : Boolean{
        val contactDBE = createContactDBE(contact, characterName)
        appDatabase.characterContactsDao().insertContact(contactDBE)

        return true
    }

    override suspend fun getAllContacts(activeCharacter: String, minStanding: Double): List<Contact>{
        val contactList = appDatabase.characterContactsDao().getContactList(activeCharacter, minStanding)
        val domainContactList = mutableListOf<Contact>()

        for(contact in contactList){
            domainContactList.add(createDomainContact(contact))
        }

        return domainContactList
    }

    private fun createContactDBE(contact: Contact, characterName: String):ContactDBE{

        return ContactDBE(contact.contactId,
            contact.contactType,
            contact.standing,
            contact.contactName,
            characterName)
    }

    private fun createDomainContact(contactDBE: ContactDBE):Contact{
        return Contact(contactDBE.contactId,
            contactDBE.contactType,
            contactDBE.standing,
            contactDBE.contactName)
    }


}