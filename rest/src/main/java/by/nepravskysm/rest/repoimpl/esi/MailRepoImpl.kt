package by.nepravskysm.rest.repoimpl.esi

import by.nepravskysm.domain.entity.InPutMail
import by.nepravskysm.domain.entity.MailMetadata
import by.nepravskysm.domain.entity.OutPutMail
import by.nepravskysm.domain.repository.rest.mail.MailRepository
import by.nepravskysm.rest.api.EsiManager
import by.nepravskysm.rest.entity.request.MailMetadataRequest
import by.nepravskysm.rest.entity.request.MailRequest
import by.nepravskysm.rest.entity.subentity.Recipient

class MailRepoImpl(private val esiManager: EsiManager) : MailRepository{


    override suspend fun getMail(accessToken: String, characterId: Long, mailId: Long): InPutMail {
        val mail = esiManager
            .getMail(accessToken,
                characterId,
                mailId)
            .await()

        return InPutMail(mail.body,
            mail.from,
            mail.labels,
            mail.read,
            mail.subject,
            mail.timestamp)
    }


    override suspend fun sendMail(
        accessToken: String,
        characterId: Long,
        outPutMail: OutPutMail
    ): Long {

        var recepientList = mutableListOf<Recipient>()
        for(recepient in outPutMail.recipients){
            recepientList.add(Recipient(recepient.id,
                recepient.type))
        }

        val mail = MailRequest(0,
            outPutMail.body,
            recepientList,
            outPutMail.subject)

        return esiManager.postMail(accessToken,
            characterId,
            mail).await()
    }

    override suspend fun updateMailMetadata(
        mailMetadata: MailMetadata,
        accessToken: String,
        characterId: Long,
        mailId: Long
    ): Boolean {
        val metadata = MailMetadataRequest(mailMetadata.labels, mailMetadata.read)
        val code = esiManager.putMailMetadata(
            metadata,
            accessToken,
            characterId,
            mailId)
            .await()
            .code()
        return code == 204
    }

    override suspend fun deleteMail(accessToken: String, characterId: Long, mailId: Long):Boolean {

        val code = esiManager.deleteMail(
            accessToken,
            characterId,
            mailId
        )
            .await()

        if (code.code() == 204) {
            return true
        }
        return false
    }
}