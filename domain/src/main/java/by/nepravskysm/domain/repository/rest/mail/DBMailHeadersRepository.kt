package by.nepravskysm.domain.repository.rest.mail

import by.nepravskysm.domain.entity.MailHeader

interface DBMailHeadersRepository{

    suspend fun saveMailsHeaders(headersList: List<MailHeader>)
    suspend fun getMailsHeaders(): List<MailHeader>
    suspend fun getLastMailId(): Long
}