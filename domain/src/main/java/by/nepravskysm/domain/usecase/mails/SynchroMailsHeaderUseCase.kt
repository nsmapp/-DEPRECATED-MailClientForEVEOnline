package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.entity.MailingList
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.database.DBMailHeadersRepository
import by.nepravskysm.domain.repository.rest.mail.MailingListRepository
import by.nepravskysm.domain.repository.rest.mail.MailsHeadersRepository
import by.nepravskysm.domain.repository.utils.NamesRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase
import by.nepravskysm.domain.utils.formaMailHeaderList
import by.nepravskysm.domain.utils.listHeadersToUniqueList
import by.nepravskysm.domain.utils.removeUnsubscrubeMailingList
import java.lang.Exception

class SynchroMailsHeaderUseCase(private val authRepository: AuthRepository,
                                private val authInfoRepository: AuthInfoRepository,
                                private val mailsHeadersRepository: MailsHeadersRepository,
                                private val namesRepository: NamesRepository,
                                private val dbMailHeadersRepository: DBMailHeadersRepository,
                                private val mailingListRepository: MailingListRepository
) : AsyncUseCase<Boolean>() {


    override suspend fun onBackground(): Boolean{

        val characterInfo = authInfoRepository.getAuthInfo()

        return try {
            getMailHeadersContainer(characterInfo.accessToken, characterInfo.characterId)

        }catch (e: Exception){
            val token = authRepository.refreshAuthToken(characterInfo.refreshToken)
            getMailHeadersContainer(token.accessToken, characterInfo.characterId)
        }
    }

    private suspend fun getMailHeadersContainer(accessToken: String, characterId: Long)
            :Boolean{

        val lastMailId: Long = dbMailHeadersRepository.getLastMailId()
        var headerList = mutableListOf<MailHeader>()

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
                }while(beforeHeaders.size == 50)
            }
        }

        if (headerList.isNotEmpty()){

            val nameMap = HashMap<Long, String>()
            val mailingList: List<MailingList> = mailingListRepository
                .getMailingList(accessToken, characterId)
            headerList = removeUnsubscrubeMailingList(headerList, mailingList)
            val characterIdList: MutableList<Long> = listHeadersToUniqueList(headerList)

            for(list in mailingList){
                characterIdList.remove(list.id)
                nameMap[list.id] = list.name
            }
            nameMap.putAll(namesRepository.getNameMap(characterIdList))
            dbMailHeadersRepository.saveMailsHeaders(formaMailHeaderList(nameMap, headerList))
        }

        return true
    }

}