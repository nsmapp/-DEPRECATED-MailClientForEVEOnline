package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.recycler.MailRecyclerAdapter
import by.nepravskysm.domain.utils.IS_READ_MAIL
import by.nepravskysm.domain.utils.MAIL_ID
import by.nepravskysm.domain.utils.FROM
import by.nepravskysm.domain.utils.SUBJECT
import kotlinx.android.synthetic.main.fragment_mails.*
import kotlinx.android.synthetic.main.fragment_mails.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

open class BaseMailListFragment : Fragment(), MailRecyclerAdapter.OnItemClickListener {




    val fragmentViewModel: MailListViewModel by sharedViewModel()
    val mailRecyclerAdapter = MailRecyclerAdapter()

    private val progresBarObserver = Observer<Boolean>{
        if(it){showProgresBar()}
        else{hideProgresBar()}
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {



        val fragmentView = inflater.inflate(R.layout.fragment_mails, container, false)

        mailRecyclerAdapter.setItemClickListener(this)
        mailRecyclerAdapter.setEntiies(fragmentViewModel.allMailsHeaderList.value!!)

        fragmentView.rootView.mailList.layoutManager = LinearLayoutManager(context)
        fragmentView.rootView.mailList.adapter = mailRecyclerAdapter
        fragmentView.rootView.progressBar.visibility = View.GONE

        fragmentViewModel.isVisibilityProgressBar.observe(this, progresBarObserver)

        return fragmentView
    }

    override fun onItemClick(mailId: Long, fromName: String, subject: String, isRead: Boolean) {
        val navController = NavHostFragment.findNavController(this)

        Log.d("logd", "$mailId $fromName $subject")
        val bundle = Bundle()
        bundle.putLong(MAIL_ID, mailId)
        bundle.putString(FROM, fromName)
        bundle.putString(SUBJECT, subject)
        bundle.putBoolean(IS_READ_MAIL, isRead)

        navController.navigate(R.id.readMailFragment, bundle)
    }

    private fun showProgresBar(){
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgresBar(){
        progressBar.visibility = View.GONE
    }


}