package by.nepravskysm.database.repoimpl

import by.nepravskysm.database.AppDatabase
import by.nepravskysm.database.entity.MailHeaderDBE
import by.nepravskysm.database.entity.subentity.RecipientDBE
import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.entity.subentity.Recipient
import by.nepravskysm.domain.repository.rest.mail.DBMailHeadersRepository

class DBMailHeaderRepoImpl(private val appDatabase: AppDatabase): DBMailHeadersRepository {


    override suspend fun getMailsHeaders(): List<MailHeader> {
        val headers = mutableListOf<MailHeader>()
        val headersList = appDatabase.mailHeadersDao()
            .getMailsHeaders()

        for (header in headersList) {

            val domainRecipients = header.recipients
            val recipients = mutableListOf<Recipient>()
            for (recipient in domainRecipients) {
                recipients.add(Recipient(recipient.id, recipient.type))
            }

            headers.add(
                MailHeader(
                    header.mailId,
                    header.fromId,
                    header.fromName,
                    header.isRead,
                    header.labels,
                    recipients,
                    header.subject,
                    header.timestamp
                )
            )
        }

        return headers
    }

    override suspend fun saveMailsHeaders(headersList: List<MailHeader>) {

        val headers = mutableListOf<MailHeaderDBE>()

        for (header in headersList) {
            val domainRecipients = header.recipients
            val recipients = mutableListOf<RecipientDBE>()
            for (recipient in domainRecipients) {
                recipients.add(RecipientDBE(recipient.id, recipient.type))
            }

            headers.add(
                MailHeaderDBE(
                    header.mailId,
                    header.fromId,
                    header.fromName,
                    header.isRead,
                    header.labels,
                    recipients,
                    header.subject,
                    header.timestamp
                )
            )
        }
        appDatabase.mailHeadersDao()
            .saveMailHeaders(headers)
    }

    override suspend fun getLastMailId(): Long = appDatabase.mailHeadersDao().getLastMailId()
}
