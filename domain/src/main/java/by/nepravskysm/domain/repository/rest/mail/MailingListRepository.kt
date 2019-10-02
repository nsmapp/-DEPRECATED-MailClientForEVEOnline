package by.nepravskysm.domain.repository.rest.mail

import by.nepravskysm.domain.entity.MailingList

interface MailingListRepository {

    suspend fun getMailingList(accessToken :String,
                               characterId :Long): List<MailingList>
}