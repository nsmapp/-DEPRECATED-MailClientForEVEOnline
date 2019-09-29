package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.MailMetadata
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.mail.MailRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class UpdataMailMetadataUseCase(private val authRepository: AuthRepository,
                                private val authInfoRepository: AuthInfoRepository,
                                private val mailRepository: MailRepository
): AsyncUseCase<Unit>() {



    private var mailId: Long = 0

    private var labelsList = mutableListOf<Int>()

    fun setData(mailId: Long, labelsList: List<Int>){
        this.labelsList.clear()
        this.labelsList.addAll(labelsList)
        this.mailId = mailId

    }

    override suspend fun onBackground() {

        val authInfo = authInfoRepository.getAuthInfo()

        try {


            mailRepository
                .updateMailMetadata(
                    MailMetadata(labelsList, true),
                    authInfo.accessToken,
                    authInfo.characterId,
                    mailId)



        }catch (e: Exception){
            val token = authRepository.refreshAuthToken(authInfo.refreshToken)


            mailRepository
                .updateMailMetadata(
                    MailMetadata(labelsList, true),
                    token.accessToken,
                    authInfo.characterId,
                    mailId)

        }

    }
}