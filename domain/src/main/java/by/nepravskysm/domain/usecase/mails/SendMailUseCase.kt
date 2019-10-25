package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.OutPutMail
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.mail.MailRepository
import by.nepravskysm.domain.repository.utils.IdsRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class SendMailUseCase(private val authRepository: AuthRepository,
                      private val authInfoRepository: AuthInfoRepository,
                      private val mailRepository: MailRepository,
                      private val idsRepository: IdsRepository,
                      private val activeCharacterRepository: ActiveCharacterRepository) : AsyncUseCase<Long>() {


    private lateinit var mail: OutPutMail
    private val names = mutableSetOf<String>()

    fun setData(outPutMail: OutPutMail, nameList: Set<String>){
        names.clear()
        mail = outPutMail
        names.addAll(nameList)
    }

    override suspend fun onBackground(): Long {

        val recipients = idsRepository.getRecepientList(names.toTypedArray())
        mail.recipients.addAll(recipients)
        if (mail.body.length > 7950) {
            mail.body = mail.body.substring(1, 7950)
        }

        val characterName = activeCharacterRepository.getActiveCharacterName()
        val authInfo = authInfoRepository.getAuthInfo(characterName)


        return try {
            mailRepository.sendMail(
                authInfo.accessToken,
                authInfo.characterId,
                mail
            )
        }catch (e: Exception){
            val token = authRepository.refreshAuthToken(authInfo.refreshToken)
            mailRepository.sendMail(
                token.accessToken,
                authInfo.characterId,
                mail)
        }

    }
}