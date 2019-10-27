package by.nepravskysm.database.repoimpl

import by.nepravskysm.database.AppDatabase
import by.nepravskysm.database.entity.MailHeaderDBE
import by.nepravskysm.database.entity.subentity.RecipientDBE
import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.entity.subentity.Recipient
import by.nepravskysm.domain.repository.database.DBMailHeadersRepository

class DBMailHeaderRepoImpl(private val appDatabase: AppDatabase):
    DBMailHeadersRepository {


    override suspend fun getUnreadMails(activeCharacter: String): List<MailHeader> {
        val unreadMails = appDatabase.mailHeadersDao().getUnreadMails(activeCharacter)
        return mapToDomainHeaders(unreadMails)
    }
    override suspend fun deleteMail(mailId: Long) {
        appDatabase.mailHeadersDao().delelteMail(mailId)
    }

    override suspend fun get(characterName: String): List<MailHeader> {
        val headersList = appDatabase.mailHeadersDao()
            .getMailsHeaders(characterName)

        return mapToDomainHeaders(headersList)
    }

    override suspend fun getWithLabels(
        characterName: String,
        labels: String, lastHeaderId: Long
    ): List<MailHeader> {
        val headersList = appDatabase.mailHeadersDao()
            .getMailsHeadersWithLabels(characterName, labels, lastHeaderId)

        return mapToDomainHeaders(headersList)
    }

    override suspend fun getInbox(characterName: String, lastHeaderId: Long): List<MailHeader> {
        return getWithLabels(characterName, "%1%", lastHeaderId)
    }

    override suspend fun getSend(characterName: String, lastHeaderId: Long): List<MailHeader> {
        return getWithLabels(characterName, "%2%", lastHeaderId)
    }

    override suspend fun getCorporation(characterName: String, lastHeaderId: Long): List<MailHeader> {
        return getWithLabels(characterName, "%4%", lastHeaderId)
    }

    override suspend fun getAlliance(characterName: String, lastHeaderId: Long): List<MailHeader> {
        return getWithLabels(characterName, "%8%", lastHeaderId)
    }

    override suspend fun getMailingList(characterName: String, lastHeaderId: Long): List<MailHeader> {
        return getWithLabels(characterName, "[]", lastHeaderId)
    }

    override suspend fun save(headersList: List<MailHeader>, characterName: String) {

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
                    header.timestamp,
                    characterName
                )
            )
        }
        appDatabase.mailHeadersDao()
            .saveMailHeaders(headers)
    }

    override suspend fun getLastMailId(activeCharacter: String): Long =
        appDatabase.mailHeadersDao().getLastMailId(activeCharacter)

    override suspend fun setMailIsRead(mailId: Long) {appDatabase.mailHeadersDao().setMailIsRead(mailId)}

    private fun mapToDomainHeaders(headersList: List<MailHeaderDBE>) : List<MailHeader>{
        val headers = mutableListOf<MailHeader>()

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

}
