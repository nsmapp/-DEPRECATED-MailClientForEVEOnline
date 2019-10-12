package by.nepravskysm.domain.repository.database

import by.nepravskysm.domain.entity.Contact

interface DBCharacterContactsRepository {

    suspend fun insertContactList(contactList: List<Contact>, characterName: String): Boolean
    suspend fun insertContact(contact: Contact, characterName: String): Boolean

    suspend fun getAllContacts(activeCharacter: String,minStanding :Double): List<Contact>
}