package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.entity.MailingList
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.database.DBMailHeadersRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.mail.MailingListRepository
import by.nepravskysm.domain.repository.rest.mail.MailsHeadersRepository
import by.nepravskysm.domain.repository.utils.NamesRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase
import by.nepravskysm.domain.utils.convertDataFormatInMailHeader

class SynchroMailsHeaderUseCase(
    private val authRepo: AuthRepository,
    private val authInfoRepo: AuthInfoRepository,
    private val mailsHeadersRepo: MailsHeadersRepository,
    private val namesRepo: NamesRepository,
    private val dbMailHeadersRepo: DBMailHeadersRepository,
    private val mailingListRepo: MailingListRepository,
    private val activeCharacterRepo: ActiveCharacterRepository
) : AsyncUseCase<Boolean>() {


    override suspend fun onBackground(): Boolean{

        val characterName = activeCharacterRepo.getActiveCharacterName()
        val characterInfo = authInfoRepo.getAuthInfo(characterName)

        return try {
            getMailHeadersContainer(characterInfo.accessToken, characterInfo.characterId, characterInfo.characterName)

        }catch (e: Exception){
            val token = authRepo.refreshAuthToken(characterInfo.refreshToken)
            getMailHeadersContainer(token.accessToken, characterInfo.characterId, characterInfo.characterName)
        }
    }

    private suspend fun getMailHeadersContainer(accessToken: String, characterId: Long, characterName: String)
            :Boolean{

        val lastMailId: Long = dbMailHeadersRepo.getLastMailId(characterName)
        var headerList = mutableListOf<MailHeader>()

        val headers = mailsHeadersRepo
            .getLast50(accessToken, characterId)
            .filter {header -> header.mailId > lastMailId }

        if (headers.isNotEmpty()){
            headerList.addAll(headers)
            if (headers.size == 50){

                var minHeaderId: Long = headers.minBy { it.mailId }!!.mailId
                do {

                    val beforeHeaders = mailsHeadersRepo
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
            val mailingList: List<MailingList> = mailingListRepo
                .getMailingList(accessToken, characterId)
            val mailingListIds = mailingList.map { it.id }
            for (list in mailingList){
                nameMap[list.id] = list.name
            }

            val mailingListHeaders = mutableListOf<MailHeader>()
            val noMailingListHeaders = mutableListOf<MailHeader>()

            for(header in headerList){
                if(header.labels.isNotEmpty()){
                    noMailingListHeaders.add(header)
                }else{
                    for (recipient in header.recipients.map { it.id }){
                        if (mailingListIds.contains(recipient)){
                            mailingListHeaders.add(header)
                        }
                    }
                }
            }

            headerList.clear()
            headerList.addAll(noMailingListHeaders)
            headerList.addAll(mailingListHeaders)

            val nameIdList = noMailingListHeaders.map { it.fromId }.distinct()
            nameMap.putAll(namesRepo.getNameMap(nameIdList))

            for (header in headerList){
                try {
                    header.fromName = nameMap[header.fromId]!!
                    convertDataFormatInMailHeader(header)
                }catch (e: Exception){

                }
            }
            dbMailHeadersRepo
                .save(headerList, characterName)
        }

        return true
    }

}