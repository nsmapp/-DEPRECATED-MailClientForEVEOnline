package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.corp


import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base.BaseMailListFragment
import org.koin.android.viewmodel.ext.android.viewModel


class CorpFragment : BaseMailListFragment<CorporationViewModel>() {

    override val fViewModel: CorporationViewModel by viewModel()

    override fun setUnreadMailConteiner(): Int = R.id.corpFragment



}