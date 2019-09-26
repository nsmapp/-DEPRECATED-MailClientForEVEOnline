package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.mail.MailRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class DeleteMailUseCase (private val authRepository: AuthRepository,
                         private val authInfoRepository: AuthInfoRepository,
                         private val mailRepository: MailRepository
): AsyncUseCase<Boolean>() {



    private var mailId: Long = 0


    fun setData(mailId: Long){
        this.mailId = mailId

    }

    override suspend fun onBackground(): Boolean{

        val authInfo = authInfoRepository.getAuthInfo()

        return try{

            mailRepository
                .deleteMail(
                    authInfo.accessToken,
                    authInfo.characterId,
                    mailId)

        }catch (e: Exception){
            val token = authRepository.refreshAuthToken(authInfo.refreshToken)

            mailRepository
                .deleteMail(
                    token.accessToken,
                    authInfo.characterId,
                    mailId)

        }

    }
}