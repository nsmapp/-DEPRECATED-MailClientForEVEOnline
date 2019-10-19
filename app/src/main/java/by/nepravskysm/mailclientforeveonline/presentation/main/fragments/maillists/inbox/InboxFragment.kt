package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.inbox



import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base.BaseMailListFragment
import org.koin.android.viewmodel.ext.android.viewModel


class InboxFragment : BaseMailListFragment<InboxViewModel>() {


    override val fViewModel: InboxViewModel by viewModel()




    override fun setUnreadMailConteiner() = R.id.inboxFragment


}