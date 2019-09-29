package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.repository.rest.mail.DBMailHeadersRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class GetMailHeadersFromDB(private val dbMailHeadersRepository: DBMailHeadersRepository)
    : AsyncUseCase<List<MailHeader>>(){

    override suspend fun onBackground(): List<MailHeader> {
        return  dbMailHeadersRepository.getMailsHeaders()
    }
}