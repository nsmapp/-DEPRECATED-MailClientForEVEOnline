package by.nepravskysm.domain.usecase.character

import by.nepravskysm.domain.entity.Contact
import by.nepravskysm.domain.repository.database.DBCharacterContactsRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class GetContactFromDBUseCase(
    private val dbCharacterContactsRepo: DBCharacterContactsRepository
) : AsyncUseCase<List<Contact>>(){


    override suspend fun onBackground(): List<Contact> {
        return dbCharacterContactsRepo.getAllContacts(0.0)
    }
}