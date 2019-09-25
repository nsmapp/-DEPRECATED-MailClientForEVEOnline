package by.nepravskysm.domain.repository.rest.mail

import by.nepravskysm.domain.entity.InPutMail
import by.nepravskysm.domain.entity.MailMetadata
import by.nepravskysm.domain.entity.OutPutMail

interface MailRepository {

    suspend fun getMail(accessToken :String,
                        characterId: Long,
                        mailId: Long
    ) : InPutMail

    suspend fun sendMail(accessToken :String,
                         characterId: Long,
                         outPutMail: OutPutMail
    ): Long


    suspend fun updateMailMetadata(mailMetadata: MailMetadata,
                                   accessToken: String,
                                   characterId: Long,
                                   mailId: Long
    )
}