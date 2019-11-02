package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.allmails

import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base.BaseMailListFragment
import org.koin.android.viewmodel.ext.android.viewModel

class AllMailsFragment : BaseMailListFragment<AllMailsViewModel>() {


    override val fViewModel: AllMailsViewModel by viewModel()


    override fun setUnreadMailConteiner(): Int = R.id.allMailsFragment
}