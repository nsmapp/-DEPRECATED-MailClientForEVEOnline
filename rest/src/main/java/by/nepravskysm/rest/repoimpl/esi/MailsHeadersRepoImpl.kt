package by.nepravskysm.rest.repoimpl.esi

import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.entity.subentity.Recipient
import by.nepravskysm.domain.repository.rest.mail.MailsHeadersRepository
import by.nepravskysm.rest.api.EsiManager
import by.nepravskysm.rest.entity.response.MailHeaderResponse
import java.util.logging.Level

class MailsHeadersRepoImpl(private val esiManager: EsiManager) :
    MailsHeadersRepository {


    override suspend fun get50BeforeId(
        authToken: String,
        characterId: Long,
        minHeaderId: Long
    ): List<MailHeader> {
        val mailsHeaders: List<MailHeaderResponse> =
            esiManager
                .getMailsHeaders(authToken, characterId, minHeaderId)
                .await()

        return convert(mailsHeaders)

    }


    override suspend fun getLast50(authToken: String, characterId: Long): List<MailHeader> {

        val mailsHeaders: List<MailHeaderResponse> =
            esiManager
                .getMailsHeaders(authToken, characterId)
                .await()

        return convert(mailsHeaders)
    }

    private fun convert(restHeaders: List<MailHeaderResponse>):List<MailHeader>{
        val domainHeadersList = mutableListOf<MailHeader>()

        for (header in restHeaders){

            val recipientsList = mutableListOf<Recipient>()

            for (recipient in header.recipientRespons){
                recipientsList.add(
                    Recipient(
                        recipient.recipientId,
                        recipient.recipientType
                    )
                )
            }

            val domainMailHeader = MailHeader(
                header.mailId,
                header.fromId,
                "",           ///Api return only sender Id
                header.isRead,
                header.labels,
                recipientsList,
                header.subject,
                header.timestamp
            )

            domainHeadersList.add(domainMailHeader)
        }

        java.util.logging.Logger.getLogger("logdHeader").log(Level.INFO, "${domainHeadersList.size}")
        return domainHeadersList
    }

}