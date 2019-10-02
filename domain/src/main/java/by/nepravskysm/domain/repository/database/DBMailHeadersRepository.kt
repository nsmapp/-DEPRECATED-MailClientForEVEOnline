package by.nepravskysm.domain.repository.database

import by.nepravskysm.domain.entity.MailHeader

interface DBMailHeadersRepository{

    suspend fun saveMailsHeaders(headersList: List<MailHeader>)
    suspend fun getMailsHeaders(): List<MailHeader>
    suspend fun getLastMailId(): Long
    suspend fun setMailIsRead(mailId: Long)
}