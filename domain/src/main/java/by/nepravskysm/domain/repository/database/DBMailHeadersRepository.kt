package by.nepravskysm.domain.repository.database

import by.nepravskysm.domain.entity.MailHeader

interface DBMailHeadersRepository{

    suspend fun saveMailsHeaders(headersList: List<MailHeader>, characterName: String)
    suspend fun getMailsHeaders(characterName: String): List<MailHeader>
    suspend fun getLastMailId(activeCharacter: String): Long
    suspend fun setMailIsRead(mailId: Long)
}