package by.nepravskysm.domain.repository.rest.character

import by.nepravskysm.domain.entity.Contact

interface CharacterContactsRepository {
    suspend fun getContacts(characterId: Long, accessToken: String): List<Contact>
}