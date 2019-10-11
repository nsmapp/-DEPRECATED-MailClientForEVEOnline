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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.recycler.MailRecyclerAdapter
import by.nepravskysm.mailclientforeveonline.utils.IS_READ_MAIL
import by.nepravskysm.mailclientforeveonline.utils.MAIL_ID
import by.nepravskysm.mailclientforeveonline.utils.FROM
import by.nepravskysm.mailclientforeveonline.utils.SUBJECT
import by.nepravskysm.mailclientforeveonline.presentation.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_mails.*
import kotlinx.android.synthetic.main.fragment_mails.view.*
import kotlinx.android.synthetic.main.item_navigation_menu.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


open class BaseMailListFragment : Fragment(),
    MailRecyclerAdapter.OnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener,
    MainActivity.LoginListener{




    val fViewModel: MailListViewModel by sharedViewModel()
    lateinit var mailRecyclerAdapter: MailRecyclerAdapter

    private val progresBarObserver = Observer<Boolean>{swipeRefresh.isRefreshing = it}
    private val unreadInboxObserver = Observer<Int>{ setUnreadMail(R.id.inboxFragment, it)}
    private val unreadCorpObserver = Observer<Int>{ setUnreadMail(R.id.corpFragment, it)}
    private val unreadAllianceObserver = Observer<Int>{ setUnreadMail(R.id.allianceFragment, it)}
    private val unreadMailingListObserver = Observer<Int>{ setUnreadMail(R.id.mailingListFragment, it)}

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val fView = inflater.inflate(R.layout.fragment_mails, container, false)

        mailRecyclerAdapter = fViewModel.mailRecyclerAdapter
        mailRecyclerAdapter.setItemClickListener(this)
        mailRecyclerAdapter.setEntiies(listOf())

        fView.mailList.layoutManager = LinearLayoutManager(context)
        fView.mailList.adapter = mailRecyclerAdapter
        fView.swipeRefresh.setOnRefreshListener(this)

        if (activity != null){
            (activity as MainActivity).setLoginListnerInterface(this)
        }

        fViewModel.isVisibilityProgressBar.observe(this, progresBarObserver)
        fViewModel.unreadInbox.observe(this, unreadInboxObserver)
        fViewModel.unreadCorp.observe(this, unreadCorpObserver)
        fViewModel.unreadAlliance.observe(this, unreadAllianceObserver)
        fViewModel.unreadMailingList.observe(this, unreadMailingListObserver)

        return fView
    }

    override fun onResume() {
        super.onResume()
        fViewModel.loadHeadersFromDB()
    }

    override fun refreshDataAfterLogin() {
        refreshData()
    }

    override fun onRefresh() {
        fViewModel.loadNewMailHeaders()
    }

    fun refreshData(){
        fViewModel.loadHeadersFromDB()
        fViewModel.loadNewMailHeaders()
    }



    private fun setUnreadMail(itemMenuId: Int, count: Int){
        if(activity != null){
            if(count != 0){
                (activity as MainActivity).navigationView.menu.findItem(itemMenuId)
                    .actionView.unreadMails.text = "$count"
            }else{
                (activity as MainActivity).navigationView.menu.findItem(itemMenuId)
                    .actionView.unreadMails.text = ""
            }
        }

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



}