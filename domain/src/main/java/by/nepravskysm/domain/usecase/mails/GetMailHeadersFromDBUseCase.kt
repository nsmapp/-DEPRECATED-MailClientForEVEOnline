package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.DBMailHeadersRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class GetMailHeadersFromDBUseCase(
    private val dbMailHeadersRepo: DBMailHeadersRepository,
    private val activeCharacterRepo: ActiveCharacterRepository
)
    : AsyncUseCase<List<MailHeader>>(){

    private var label = 0

    fun setOnlyInbox(): GetMailHeadersFromDBUseCase{
        label = 101
        return this
    }

    fun setOnlySend(): GetMailHeadersFromDBUseCase{
        label = 102
        return this
    }
    fun setOnlyCorporation(): GetMailHeadersFromDBUseCase{
        label = 104
        return this
    }
    fun setOnlyAlliance(): GetMailHeadersFromDBUseCase{
        label = 108
        return this
    }

    fun setOnlyMailingList(): GetMailHeadersFromDBUseCase{
        label = 100
        return this
    }

    override suspend fun onBackground(): List<MailHeader> {
        val characterName = activeCharacterRepo.getActiveCharacterName()

        return when(label){
            0 -> dbMailHeadersRepo.get(characterName)
            101 -> dbMailHeadersRepo.getInbox(characterName)
            102 -> dbMailHeadersRepo.getSend(characterName)
            104 -> dbMailHeadersRepo.getCorporation(characterName)
            108 -> dbMailHeadersRepo.getAlliance(characterName)
            100 -> dbMailHeadersRepo.getMailingList(characterName)
            else -> dbMailHeadersRepo.get(characterName)
        }
    }

}