package by.nepravskysm.domain.repository.database

import by.nepravskysm.domain.entity.MailHeader

interface DBMailHeadersRepository{

    suspend fun save(headersList: List<MailHeader>, characterName: String)
    suspend fun get(characterName: String): List<MailHeader>
    suspend fun getWithLabels(characterName: String,
                              labels: String,
                              lastHeaderId: Long = 999999999999): List<MailHeader>
    suspend fun getInbox(characterName: String,
                         lastHeaderId: Long = 999999999999): List<MailHeader>
    suspend fun getSend(characterName: String,
                        lastHeaderId: Long = 9999999999999): List<MailHeader>
    suspend fun getCorporation(characterName: String,
                               lastHeaderId: Long = 999999999999): List<MailHeader>
    suspend fun getAlliance(characterName: String,
                            lastHeaderId: Long = 999999999999): List<MailHeader>
    suspend fun getMailingList(characterName: String,
                               lastHeaderId: Long = 999999999999 ): List<MailHeader>
    suspend fun getLastMailId(activeCharacter: String): Long
    suspend fun setMailIsRead(mailId: Long)
    suspend fun deleteMail(mailId: Long)
    suspend fun getUnreadMails(activeCharacter: String):List<MailHeader>
}