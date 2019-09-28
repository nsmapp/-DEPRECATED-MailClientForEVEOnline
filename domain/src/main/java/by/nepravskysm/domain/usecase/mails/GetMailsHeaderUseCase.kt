package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.repository.database.AuthInfoRepository
import by.nepravskysm.domain.repository.rest.auth.AuthRepository
import by.nepravskysm.domain.repository.rest.mail.DBMailHeadersRepository
import by.nepravskysm.domain.repository.rest.mail.MailsHeadersRepository
import by.nepravskysm.domain.repository.utils.NamesRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Level

class GetMailsHeaderUseCase(private val authRepository: AuthRepository,
                            private val authInfoRepository: AuthInfoRepository,
                            private val mailsHeadersRepository: MailsHeadersRepository,
                            private val namesRepository: NamesRepository,
                            private val dbMailHeadersRepository: DBMailHeadersRepository
) : AsyncUseCase<List<MailHeader>>() {


    override suspend fun onBackground(): List<MailHeader> {

        val characterInfo = authInfoRepository.getAuthInfo()

        java.util.logging.Logger.getLogger("logdOnError").log(Level.INFO, "++++------11111++++++++")
        return try {

            getMailHeadersContainer(characterInfo.accessToken, characterInfo.characterId)


        }catch (e: Exception){
            val token = authRepository.refreshAuthToken(characterInfo.refreshToken)

            getMailHeadersContainer(token.accessToken, characterInfo.characterId)

        }

    }



    private suspend fun getMailHeadersContainer(accessToken: String, characterId: Long)
            :List<MailHeader>{

        val headerList = mailsHeadersRepository.getLast50MailsHeaders(accessToken, characterId)
        java.util.logging.Logger.getLogger("logdOnError").log(Level.INFO, "+++++++++++00000++++++++")
        val characterIdArray = getCharacterIdSet(headerList).toTypedArray()
        val nameMap = namesRepository.getNameMap(characterIdArray)
        java.util.logging.Logger.getLogger("logdOnError").log(Level.INFO, "+++++++++++11111++++++++")

        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
        inputDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        var dayDateFormat = SimpleDateFormat("HH:mm dd.mm.yy")
        dayDateFormat.timeZone = TimeZone.getTimeZone("UTC")

        var day = ""

        for (header in headerList){
            day = dayDateFormat.format(inputDateFormat.parse(header.timestamp))
            header.fromName = nameMap[header.fromId].toString()
            header.timestamp = day
        }
        java.util.logging.Logger.getLogger("logdOnError").log(Level.INFO, "++++++++++222222+++++++++")

        dbMailHeadersRepository.saveMailsHeaders(headerList)

        return dbMailHeadersRepository.getMailsHeaders()
    }

    private fun getCharacterIdSet(headerList: List<MailHeader>): MutableSet<Long>{

        val idSet = mutableSetOf<Long>()
        for (header in headerList){
            idSet.add(header.fromId)
        }

        return idSet
    }
}