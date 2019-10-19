package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.send

import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base.BaseMailListFragment
import org.koin.android.viewmodel.ext.android.viewModel

class SendFragment : BaseMailListFragment<SendViewModel>(){

    override val fViewModel: SendViewModel by viewModel()

    override fun setUnreadMailConteiner(): Int = R.id.sendFragment


}