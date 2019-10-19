package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.mailinglist

import by.nepravskysm.domain.usecase.mails.GetMailHeadersAfterIdFromDBUseCase
import by.nepravskysm.domain.usecase.mails.GetMailHeadersFromDBUseCase
import by.nepravskysm.domain.usecase.mails.GetNewMailHeadersUseCase
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base.BaseMailListViewModel

class MailingListViewModel(getMailHeadersFromDBUseCase: GetMailHeadersFromDBUseCase,
                           getNewMailHeadersUseCase: GetNewMailHeadersUseCase,
                           getMailHeadersAfterIdFromDB: GetMailHeadersAfterIdFromDBUseCase
) : BaseMailListViewModel(
    getMailHeadersFromDBUseCase, getNewMailHeadersUseCase, getMailHeadersAfterIdFromDB
){

    override fun setMailHeaderLabel() {
        getMailHeadersFromDB.setOnlyMailingList()
        getMailHeadersAfterIdFromDB.setOnlyMailingList()
    }
}