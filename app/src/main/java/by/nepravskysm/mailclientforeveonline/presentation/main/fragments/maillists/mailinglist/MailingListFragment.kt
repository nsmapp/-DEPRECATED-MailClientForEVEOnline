package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.mailinglist

import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base.BaseMailListFragment
import org.koin.android.viewmodel.ext.android.viewModel

class MailingListFragment : BaseMailListFragment<MailingListViewModel>(){

    override val fViewModel: MailingListViewModel by viewModel()
    override fun setUnreadMailConteiner(): Int = R.id.mailingListFragment

}