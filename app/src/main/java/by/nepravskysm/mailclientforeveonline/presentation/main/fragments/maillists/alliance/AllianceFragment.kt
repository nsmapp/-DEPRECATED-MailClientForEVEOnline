package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.alliance


import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base.BaseMailListFragment
import org.koin.android.viewmodel.ext.android.viewModel

class AllianceFragment : BaseMailListFragment<AllianceViewModel>(){

    override val fViewModel: AllianceViewModel by viewModel()

    override fun setUnreadMailConteiner(): Int = R.id.allianceFragment


}