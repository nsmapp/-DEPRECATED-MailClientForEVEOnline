package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.DBMailHeadersRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class GetMailHeadersAfterIdFromDBUseCase(private val dbMailHeadersRepo: DBMailHeadersRepository,
                                         private val activeCharacterRepo: ActiveCharacterRepository
) : AsyncUseCase<List<MailHeader>>(){

    private var lastMailId:Long = 0

    fun setData(lastMailId:Long): GetMailHeadersAfterIdFromDBUseCase{
        this.lastMailId = lastMailId
        return this
    }

    private var label = 0

    fun setOnlyInbox(): GetMailHeadersAfterIdFromDBUseCase{
        label = 101
        return this
    }

    fun setOnlySend(): GetMailHeadersAfterIdFromDBUseCase{
        label = 102
        return this
    }
    fun setOnlyCorporation(): GetMailHeadersAfterIdFromDBUseCase{
        label = 104
        return this
    }
    fun setOnlyAlliance(): GetMailHeadersAfterIdFromDBUseCase{
        label = 108
        return this
    }

    fun setOnlyMailingList(): GetMailHeadersAfterIdFromDBUseCase{
        label = 100
        return this
    }
    override suspend fun onBackground(): List<MailHeader> {
        val characterName = activeCharacterRepo.getActiveCharacterName()

        if(lastMailId != 0L){
            return when(label){
                101 -> dbMailHeadersRepo.getInbox(characterName, lastMailId)
                102 -> dbMailHeadersRepo.getSend(characterName, lastMailId)
                104 -> dbMailHeadersRepo.getCorporation(characterName, lastMailId)
                108 -> dbMailHeadersRepo.getAlliance(characterName, lastMailId)
                100 -> dbMailHeadersRepo.getMailingList(characterName, lastMailId)
                else -> emptyList()
            }
        }
        return emptyList()

    }
}