package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.mail.MailsHeadersRepository
import by.nepravskysm.domain.repository.utils.NamesRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class GetMailsHeaderUseCase(private val authRepository: AuthRepository,
                            private val authInfoRepository: AuthInfoRepository,
                            private val mailsHeadersRepository: MailsHeadersRepository,
                            private val namesRepository: NamesRepository
) : AsyncUseCase<List<MailHeader>>() {


    override suspend fun onBackground(): List<MailHeader> {

        val characterInfo = authInfoRepository.getAuthInfo()


        return try {

            getMailHeadersContainer(characterInfo.accessToken, characterInfo.characterId)


        }catch (e: Exception){
            val token = authRepository.refreshAuthToken(characterInfo.refreshToken)

            getMailHeadersContainer(token.accessToken, characterInfo.characterId)

        }

    }



    private suspend fun getMailHeadersContainer(accessToken: String, characterId: Long)
            :List<MailHeader>{

        val headerList = mailsHeadersRepository.getLastMailsHeaders(accessToken, characterId)
        val characterIdArray = getCharacterIdSet(headerList).toTypedArray()
        val nameMap = namesRepository.getNameMap(characterIdArray)

        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
        inputDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        var dayDateFormat = SimpleDateFormat("HH:mm dd.mm.yy")
        dayDateFormat.timeZone = TimeZone.getTimeZone("UTC")

        var day = ""

        for (header in headerList){
            day = dayDateFormat.format(inputDateFormat.parse(header.timestamp))
            header.fromName = nameMap[header.from].toString()
            header.timestamp = day
        }

        return headerList
    }

    private fun getCharacterIdSet(headerList: List<MailHeader>): MutableSet<Long>{

        val idSet = mutableSetOf<Long>()
        for (header in headerList){
            idSet.add(header.from)
        }

        return idSet
    }
}