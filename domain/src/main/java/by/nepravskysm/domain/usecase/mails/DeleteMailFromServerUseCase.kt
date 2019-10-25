package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.mail.MailRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class DeleteMailFromServerUseCase(
    private val authRepo: AuthRepository,
    private val authInfoRepo: AuthInfoRepository,
    private val mailRepo: MailRepository,
    private val activeCharacterRepo: ActiveCharacterRepository
): AsyncUseCase<Boolean>() {

    private var mailId: Long = 0

    fun setData(mailId: Long): DeleteMailFromServerUseCase {
        this.mailId = mailId
        return this
    }

    override suspend fun onBackground(): Boolean{

        val characterName = activeCharacterRepo.getActiveCharacterName()
        val authInfo = authInfoRepo.getAuthInfo(characterName)

        return try{

            mailRepo
                .deleteMail(
                    authInfo.accessToken,
                    authInfo.characterId,
                    mailId)

        }catch (e: Exception){
            val token = authRepo.refreshAuthToken(authInfo.refreshToken)

            mailRepo
                .deleteMail(
                    token.accessToken,
                    authInfo.characterId,
                    mailId)

        }

    }
}