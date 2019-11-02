package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.DBMailHeadersRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class UpdateAllMailMetadataDBUseCase(
    private val dbMailHeadersRepo: DBMailHeadersRepository,
    private val activeCharacterRepo: ActiveCharacterRepository
) : AsyncUseCase<Boolean>() {


    override suspend fun onBackground(): Boolean {
        val activeCharacter = activeCharacterRepo.getActiveCharacterName()
        dbMailHeadersRepo.setAllMailsAsRead(activeCharacter)
        return true
    }
}