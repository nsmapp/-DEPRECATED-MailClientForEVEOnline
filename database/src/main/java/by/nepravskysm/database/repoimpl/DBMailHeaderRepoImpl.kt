package by.nepravskysm.database.repoimpl

import android.util.Log
import by.nepravskysm.database.AppDatabase
import by.nepravskysm.database.entity.MailHeaderDBE
import by.nepravskysm.database.entity.subentity.RecipientDBE
import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.entity.subentity.Recipient
import by.nepravskysm.domain.repository.database.DBMailHeadersRepository

class DBMailHeaderRepoImpl(private val appDatabase: AppDatabase):
    DBMailHeadersRepository {


    override suspend fun getMailsHeaders(characterName: String): List<MailHeader> {
        val headers = mutableListOf<MailHeader>()
        val headersList = appDatabase.mailHeadersDao()
            .getMailsHeaders(characterName)

        Log.d("logd", "getMails ${headersList.size}");

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

    override suspend fun saveMailsHeaders(headersList: List<MailHeader>, characterName: String) {

        val headers = mutableListOf<MailHeaderDBE>()

        Log.d("logd", "saveMails ${headersList.size}");

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

    override suspend fun getLastMailId(activeCharacter: String): Long = appDatabase.mailHeadersDao().getLastMailId(activeCharacter)

    override suspend fun setMailIsRead(mailId: Long) {
        appDatabase.mailHeadersDao().setMailIsRead(mailId)
    }
}
