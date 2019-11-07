package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.MailMetadata
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.mail.MailRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class UpdataMailMetadataUseCase(
    private val authRepo: AuthRepository,
    private val authInfoRepo: AuthInfoRepository,
    private val mailRepo: MailRepository,
    private val activeCharacterRepo: ActiveCharacterRepository
): AsyncUseCase<Boolean>() {

    private var mailId: Long = 0
    private var labelsList = mutableListOf<Int>()

    fun setData(mailId: Long, labelsList: List<Int>): UpdataMailMetadataUseCase{
        this.labelsList.clear()
        this.labelsList.addAll(labelsList)
        this.mailId = mailId

        return this
    }

    override suspend fun onBackground():Boolean {

        val characterName = activeCharacterRepo.getActiveCharacterName()
        val authInfo = authInfoRepo.getAuthInfo(characterName)

        val isSucces = mailRepo
            .updateMailMetadata(
                MailMetadata(labelsList, true),
                authInfo.accessToken,
                authInfo.characterId,
                mailId
            )

        return if (isSucces) true
        else {
            val token = authRepo.refreshAuthToken(authInfo.refreshToken)
            mailRepo.updateMailMetadata(
                MailMetadata(labelsList, true),
                token.accessToken,
                authInfo.characterId,
                mailId
            )
        }
    }
}