package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.DBMailHeadersRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class GetMailHeadersFromDB(private val dbMailHeadersRepository: DBMailHeadersRepository,
                           private val activeCharacterRepository: ActiveCharacterRepository)
    : AsyncUseCase<List<MailHeader>>(){

    override suspend fun onBackground(): List<MailHeader> {
        val characterName = activeCharacterRepository.getActiveCharacterName()
        return  dbMailHeadersRepository.getMailsHeaders(characterName)
    }
}