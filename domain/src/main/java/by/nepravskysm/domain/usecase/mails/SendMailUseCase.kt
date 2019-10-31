package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.OutPutMail
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.mail.MailRepository
import by.nepravskysm.domain.repository.utils.IdsRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class SendMailUseCase(
    private val authRepo: AuthRepository,
    private val authInfoRepo: AuthInfoRepository,
    private val mailRepo: MailRepository,
    private val idsRepo: IdsRepository,
    private val activeCharacterRepo: ActiveCharacterRepository
) : AsyncUseCase<Long>() {


    private lateinit var mail: OutPutMail
    private val names = mutableSetOf<String>()

    fun setData(outPutMail: OutPutMail, nameList: Set<String>): SendMailUseCase {
        names.clear()
        mail = outPutMail
        names.addAll(nameList)
        return this
    }

    override suspend fun onBackground(): Long {

        val recipients = idsRepo.getRecepientList(names.toTypedArray())
        mail.recipients.addAll(recipients)
        if (mail.body.length > 7950) {
            mail.body = mail.body.substring(1, 7950)
        }

        val characterName = activeCharacterRepo.getActiveCharacterName()
        val authInfo = authInfoRepo.getAuthInfo(characterName)

        return try {
            mailRepo.sendMail(
                authInfo.accessToken,
                authInfo.characterId,
                mail
            )
        }catch (e: Exception){
            val token = authRepo.refreshAuthToken(authInfo.refreshToken)
            mailRepo.sendMail(
                token.accessToken,
                authInfo.characterId,
                mail)
        }

    }
}