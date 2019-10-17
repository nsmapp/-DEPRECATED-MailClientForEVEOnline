package by.nepravskysm.domain.usecase.character

import by.nepravskysm.domain.entity.Contact
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.DBCharacterContactsRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class GetContactFromDBUseCase(private val activeCharacterRepository: ActiveCharacterRepository,
                              private val dbCharacterContactsRepository: DBCharacterContactsRepository
) : AsyncUseCase<List<Contact>>(){


    override suspend fun onBackground(): List<Contact> {
        val activeCharacter = activeCharacterRepository.getActiveCharacterName()

        return dbCharacterContactsRepository.getAllContacts(activeCharacter, 0.0)
    }
}