package by.nepravskysm.domain.repository.rest.mail

import by.nepravskysm.domain.entity.MailHeader

interface MailsHeadersRepository {


    suspend fun getLastMailsHeaders(authToken :String, characterId: Long): List<MailHeader>
}