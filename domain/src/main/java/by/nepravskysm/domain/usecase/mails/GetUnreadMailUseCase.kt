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
            var labels = listOf<Int>()
            for (header in unreadMailList) {
                labels = header.labels
                if (labels.contains(2)) {
                    continue
                }
                if (labels.contains(1)) {
                    unreadMailsCount.inbox++
                    continue
                }
                if (labels.contains(4)) {
                    unreadMailsCount.corporation++
                    continue
                }
                if (labels.contains(8)) {
                    unreadMailsCount.alliance++
                    continue
                }
                if (labels.isEmpty()) {
                    unreadMailsCount.mailingList++
                    continue
                }
            }
        }

        return unreadMailsCount
    }

}