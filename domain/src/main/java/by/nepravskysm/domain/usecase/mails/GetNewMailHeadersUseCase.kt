package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.mail.DBMailHeadersRepository
import by.nepravskysm.domain.repository.rest.mail.MailsHeadersRepository
import by.nepravskysm.domain.repository.utils.NamesRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase
import by.nepravskysm.domain.utils.formaMailHeaderList
import by.nepravskysm.domain.utils.listHeadersToUniqueAttay
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class GetNewMailHeadersUseCase(private val authRepository: AuthRepository,
                               private val authInfoRepository: AuthInfoRepository,
                               private val mailsHeadersRepository: MailsHeadersRepository,
                               private val namesRepository: NamesRepository,
                               private val dbMailHeadersRepository: DBMailHeadersRepository
): AsyncUseCase<List<MailHeader>>(){


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

        val lastMailId: Long = dbMailHeadersRepository.getLastMailId()
        val headerList = mutableListOf<MailHeader>()

        val headers = mailsHeadersRepository
            .getLast50(accessToken, characterId)
            .filter {header -> header.mailId > lastMailId }

        if (headers.isNotEmpty()){
            headerList.addAll(headers)
            if (headers.size == 50){

                var minHeaderId: Long = headers.minBy { it.mailId }!!.mailId
                do {
                    val beforeHeaders = mailsHeadersRepository
                        .get50BeforeId(accessToken, characterId, minHeaderId)
                        .filter {header -> header.mailId > lastMailId }

                    if (beforeHeaders.isEmpty()){break}
                    else{
                        headerList.addAll(beforeHeaders)
                        minHeaderId = beforeHeaders.minBy { it.mailId }!!.mailId
                    }


                }while(lastMailId < minHeaderId)
            }
        }

        if (headerList.isNotEmpty()){
            val characterIdArray = listHeadersToUniqueAttay(headerList)
            val nameMap = namesRepository.getNameMap(characterIdArray)
            dbMailHeadersRepository
                .saveMailsHeaders(formaMailHeaderList(nameMap, headerList))
        }

        return dbMailHeadersRepository.getMailsHeaders()
    }


}