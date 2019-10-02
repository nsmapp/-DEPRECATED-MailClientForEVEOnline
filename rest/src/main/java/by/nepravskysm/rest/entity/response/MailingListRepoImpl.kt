package by.nepravskysm.rest.entity.response

import by.nepravskysm.domain.entity.MailingList
import by.nepravskysm.domain.repository.rest.mail.MailingListRepository
import by.nepravskysm.rest.api.EsiManager

class MailingListRepoImpl(private val esiManager: EsiManager): MailingListRepository {

    override suspend fun getMailingList(accessToken: String, characterId: Long): List<MailingList> {

        val mailingList: List<MailingListResponse> =
            esiManager.getMailList(accessToken, characterId)
                .await()

        val domainMailingList = mutableListOf<MailingList>()

        for (list in mailingList){
            domainMailingList.add(MailingList(list.mailingListId, list.name))
        }

        return domainMailingList
    }
}