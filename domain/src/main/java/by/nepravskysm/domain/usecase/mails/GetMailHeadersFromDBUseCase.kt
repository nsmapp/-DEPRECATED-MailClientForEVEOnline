package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.DBMailHeadersRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class GetMailHeadersFromDBUseCase(private val dbMailHeadersRepository: DBMailHeadersRepository,
                                  private val activeCharacterRepository: ActiveCharacterRepository)
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
        val characterName = activeCharacterRepository.getActiveCharacterName()

        return when(label){
            0 -> dbMailHeadersRepository.get(characterName)
            101 -> dbMailHeadersRepository.getInbox(characterName)
            102 -> dbMailHeadersRepository.getSend(characterName)
            104 -> dbMailHeadersRepository.getCorporation(characterName)
            108 -> dbMailHeadersRepository.getAlliance(characterName)
            100 -> dbMailHeadersRepository.getMailingList(characterName)
            else -> dbMailHeadersRepository.get(characterName)
        }
    }
}