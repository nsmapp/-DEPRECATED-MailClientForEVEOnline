package by.nepravskysm.domain.repository.rest.mail

import by.nepravskysm.domain.entity.MailHeader

interface MailsHeadersRepository {


    suspend fun getLast50(authToken :String, characterId: Long): List<MailHeader>

    suspend fun get50BeforeId(authToken :String,
                              characterId: Long,
                              minHeaderId:Long): List<MailHeader>
}