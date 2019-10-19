package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.send

import by.nepravskysm.domain.usecase.mails.GetMailHeadersAfterIdFromDBUseCase
import by.nepravskysm.domain.usecase.mails.GetMailHeadersFromDBUseCase
import by.nepravskysm.domain.usecase.mails.GetNewMailHeadersUseCase
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base.BaseMailListViewModel

class SendViewModel(getMailHeadersFromDBUseCase: GetMailHeadersFromDBUseCase,
                    getNewMailHeadersUseCase: GetNewMailHeadersUseCase,
                    getMailHeadersAfterIdFromDB: GetMailHeadersAfterIdFromDBUseCase
)
    : BaseMailListViewModel(
    getMailHeadersFromDBUseCase, getNewMailHeadersUseCase, getMailHeadersAfterIdFromDB
){
    override fun setMailHeaderLabel() {
        getMailHeadersFromDB.setOnlySend()
        getMailHeadersAfterIdFromDB.setOnlySend()
    }

}