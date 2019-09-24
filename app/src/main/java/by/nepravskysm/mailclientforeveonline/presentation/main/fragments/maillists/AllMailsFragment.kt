package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base.BaseMailListFragment


class AllMailsFragment : BaseMailListFragment(){



    private val mailHeaderListObserver = Observer<List<MailHeader>>{mailHeaderList ->
        mailRecyclerAdapter.setEntiies(mailHeaderList)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        fragmentViewModel.allMailsHeaderList.observe(this, mailHeaderListObserver)

        return super.onCreateView(inflater, container, savedInstanceState)
    }




}