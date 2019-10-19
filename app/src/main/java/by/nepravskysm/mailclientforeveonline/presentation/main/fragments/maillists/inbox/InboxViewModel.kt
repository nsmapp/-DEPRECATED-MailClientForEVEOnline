package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.inbox

import by.nepravskysm.domain.usecase.mails.GetMailHeadersAfterIdFromDBUseCase
import by.nepravskysm.domain.usecase.mails.GetMailHeadersFromDBUseCase
import by.nepravskysm.domain.usecase.mails.GetNewMailHeadersUseCase
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base.BaseMailListViewModel

class InboxViewModel(getMailHeadersFromDBUseCase: GetMailHeadersFromDBUseCase,
                     getNewMailHeadersUseCase: GetNewMailHeadersUseCase,
                     getMailHeadersAfterIdFromDB: GetMailHeadersAfterIdFromDBUseCase
) : BaseMailListViewModel(
   getMailHeadersFromDBUseCase, getNewMailHeadersUseCase, getMailHeadersAfterIdFromDB
){

    override fun setMailHeaderLabel() {
        getMailHeadersFromDB.setOnlyInbox()
        getMailHeadersAfterIdFromDB.setOnlyInbox()
    }

}