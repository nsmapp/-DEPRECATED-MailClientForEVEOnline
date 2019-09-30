package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.MainActivity
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base.BaseMailListFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_navigation_menu.view.*


class InboxFragment : BaseMailListFragment(){



    private val characterHeaderListObserver = Observer<List<MailHeader>>{mailHeaderList ->
        mailRecyclerAdapter.setEntiies(mailHeaderList)

    }

    override fun onResume() {
        super.onResume()


        try {

            mailRecyclerAdapter.setEntiies(fViewModel.inboxHeaderList.value!!)
        }catch (e:Exception){
            
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fViewModel.inboxHeaderList.observe(this, characterHeaderListObserver)

        return super.onCreateView(inflater, container, savedInstanceState)
    }





}