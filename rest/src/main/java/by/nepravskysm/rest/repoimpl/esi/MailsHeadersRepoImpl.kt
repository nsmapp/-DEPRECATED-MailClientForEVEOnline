package by.nepravskysm.rest.repoimpl.esi

import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.entity.subentity.Recipient
import by.nepravskysm.domain.repository.rest.mail.MailsHeadersRepository
import by.nepravskysm.rest.api.EsiManager
import by.nepravskysm.rest.entity.response.MailHeaderResponse
import java.util.logging.Level

class MailsHeadersRepoImpl(private val esiManager: EsiManager) :
    MailsHeadersRepository {


    override suspend fun getLastMailsHeaders(authToken: String, characterId: Long): List<MailHeader> {

        val mailsHeaders: List<MailHeaderResponse> = esiManager.getMailsHeaders(authToken, characterId).await()

        val domainHeadersList = mutableListOf<MailHeader>()

        for (header in mailsHeaders){

            val recipientsList = mutableListOf<Recipient>()

            for (recipient in header.recipientRespons){
                recipientsList.add(
                    Recipient(
                        recipient.recipientId,
                        recipient.recipientType
                    )
                )
            }

            val domainMailHeader = MailHeader(header.from,
                header.isRead,
                header.labels,
                header.mailId,
                recipientsList,
                header.subject,
                header.timestamp)

            domainHeadersList.add(domainMailHeader)
        }

        java.util.logging.Logger.getLogger("logdHeader").log(Level.INFO, "${domainHeadersList.size}")
        return domainHeadersList
    }


}