package by.nepravskysm.domain.repository.rest.mail

import by.nepravskysm.domain.entity.MailHeader

interface MailsHeadersRepository {


    suspend fun getLast50MailsHeaders(authToken :String, characterId: Long): List<MailHeader>
}