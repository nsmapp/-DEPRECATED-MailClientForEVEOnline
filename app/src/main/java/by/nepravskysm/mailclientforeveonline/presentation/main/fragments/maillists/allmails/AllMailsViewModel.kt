package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.allmails

import by.nepravskysm.domain.usecase.mails.GetMailHeadersAfterIdFromDBUseCase
import by.nepravskysm.domain.usecase.mails.GetMailHeadersFromDBUseCase
import by.nepravskysm.domain.usecase.mails.GetNewMailHeadersUseCase
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base.BaseMailListViewModel

class AllMailsViewModel(
    getMailHeadersFromDB: GetMailHeadersFromDBUseCase,
    getNewMailHeaders: GetNewMailHeadersUseCase,
    getMailHeadersAfterIdFromDB: GetMailHeadersAfterIdFromDBUseCase
) : BaseMailListViewModel(
    getMailHeadersFromDB, getNewMailHeaders, getMailHeadersAfterIdFromDB
) {

    override fun setMailHeaderLabel() {
        getMailHeadersFromDB.setAllMails()
        getMailHeadersAfterIdFromDB.setAllMails()
    }
}