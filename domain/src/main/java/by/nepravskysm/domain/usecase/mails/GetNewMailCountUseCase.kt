package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.AuthInfo
import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.database.DBMailHeadersRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.mail.MailsHeadersRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class GetNewMailCountUseCase(
    private val authRepository: AuthRepository,
    private val authInfoRepository: AuthInfoRepository,
    private val mailsHeadersRepository: MailsHeadersRepository,
    private val dbMailHeadersRepository: DBMailHeadersRepository,
    private val activeCharacterRepository: ActiveCharacterRepository
) : AsyncUseCase<Int>() {

    override suspend fun onBackground(): Int {

        val characterName = activeCharacterRepository.getActiveCharacterName()
        val characterInfo: AuthInfo = authInfoRepository.getAuthInfo(characterName)
        var newMailCount: Int
        newMailCount = try {
            getNewMailCount(characterInfo.accessToken, characterInfo.characterId, characterName)
        } catch (e: Exception) {
            val token = authRepository.refreshAuthToken(characterInfo.refreshToken).accessToken
            getNewMailCount(token, characterInfo.characterId, characterName)
        }
        return newMailCount
    }

    private suspend fun getNewMailCount(
        accessToken: String,
        characterId: Long,
        characterName: String
    ): Int {

        val lastHeaderId = dbMailHeadersRepository.getLastMailId(characterName)
        val newHeaderList = mutableListOf<MailHeader>()
        val mailHeadersList = mailsHeadersRepository
            .getLast50(accessToken, characterId).filter { header ->
                header.mailId > lastHeaderId &&
                        !header.labels.contains(2)
            }
        if (mailHeadersList.isNotEmpty()) {
            newHeaderList.addAll(mailHeadersList)

            if (mailHeadersList.size == 50) {
                var minHeaderId: Long = mailHeadersList.minBy { it.mailId }!!.mailId
                do {
                    val beforeHeaders = mailsHeadersRepository
                        .get50BeforeId(accessToken, characterId, minHeaderId)
                        .filter { header ->
                            header.mailId > lastHeaderId && !header.labels.contains(2)
                        }

                    if (beforeHeaders.isEmpty()) {
                        break
                    } else {
                        newHeaderList.addAll(beforeHeaders)
                        minHeaderId = beforeHeaders.minBy { it.mailId }!!.mailId
                    }
                } while (lastHeaderId < minHeaderId || beforeHeaders.size == 50)
            }
        }
        return newHeaderList.size
    }

}