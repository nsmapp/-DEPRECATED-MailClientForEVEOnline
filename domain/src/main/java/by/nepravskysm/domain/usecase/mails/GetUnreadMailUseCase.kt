package by.nepravskysm.domain.usecase.mails

import by.nepravskysm.domain.entity.UnreadMailsCount
import by.nepravskysm.domain.repository.database.ActiveCharacterRepository
import by.nepravskysm.domain.repository.database.DBMailHeadersRepository
import by.nepravskysm.domain.usecase.base.AsyncUseCase

class GetUnreadMailUseCase(private val activeCharacterRepo: ActiveCharacterRepository,
                           private val dbMailHeadersRepo: DBMailHeadersRepository)
    : AsyncUseCase<UnreadMailsCount>(){

    override suspend fun onBackground(): UnreadMailsCount {
        val activeCharacter = activeCharacterRepo.getActiveCharacterName()
        val unreadMailList = dbMailHeadersRepo.getUnreadMails(activeCharacter)
        val unreadMailsCount = UnreadMailsCount()

        if(unreadMailList.isNotEmpty()){
            for (header in unreadMailList) {
                if(header.labels.contains(1)){unreadMailsCount.inbox++}
                if(header.labels.contains(4)){unreadMailsCount.corporation++}
                if(header.labels.contains(8)){unreadMailsCount.alliance++}
                if(header.labels.isEmpty()){unreadMailsCount.mailingList++}
            }
        }

        return unreadMailsCount
    }

}