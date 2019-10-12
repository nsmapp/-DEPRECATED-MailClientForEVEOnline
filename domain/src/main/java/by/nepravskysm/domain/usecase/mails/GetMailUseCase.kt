package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.InPutMail
import by.nepravskysm.domain.entity.MailMetadata
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.mail.MailRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class GetMailUseCase(private val authRepository: AuthRepository,
                     private val authInfoRepository: AuthInfoRepository,
                     private val mailRepository: MailRepository,
                     private val activeCharacterRepository: ActiveCharacterRepository): AsyncUseCase<InPutMail> (){

    private var mailId: Long = 0

    fun setMailId(id: Long){
        mailId = id
    }

    override suspend fun onBackground(): InPutMail {

        val characterName = activeCharacterRepository.getActiveCharacterName()
        val authInfo = authInfoRepository.getAuthInfo(characterName)

        try {
            val mail = mailRepository.getMail(
                authInfo.accessToken,
                authInfo.characterId,
                mailId)


            return mail

        }catch (e: Exception){
            val token = authRepository.refreshAuthToken(authInfo.refreshToken)
            val mail = mailRepository.getMail(
                token.accessToken,
                authInfo.characterId,
                mailId)



            return mail
        }

    }

}