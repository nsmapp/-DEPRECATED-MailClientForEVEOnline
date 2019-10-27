package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.MainActivity
import by.nepravskysm.mailclientforeveonline.utils.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_mails.*
import kotlinx.android.synthetic.main.fragment_mails.view.*
import kotlinx.android.synthetic.main.item_mail_info.view.*

abstract class BaseMailListFragment <VM : BaseMailListViewModel>: Fragment(),
SwipeRefreshLayout.OnRefreshListener,
MainActivity.LoginListener{

    abstract val fViewModel:VM
    val mailRecyclerAdapter = RecyclerAdapter()

    private val progresBarObserver = Observer<Boolean>{swipeRefresh.isRefreshing = it}
    //    private val unreadMailObserver = Observer<Int>{ setUnreadMail(setUnreadMailConteiner(), it)}
    private val mailHeaderObserver = Observer<List<MailHeader>> {
        mailRecyclerAdapter.setData(it)
        (activity as MainActivity).setUnreadMailsCount()
    }
    private val updateMailHeaderObserver = Observer<List<MailHeader>>{
        mailRecyclerAdapter.addData(it)
    }
    private val errorObserver =
        Observer<Long> { eventId -> makeToastMessage((activity as MainActivity), eventId) }

    abstract fun setUnreadMailConteiner(): Int


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val fView = inflater.inflate(R.layout.fragment_mails, container, false)
        mailRecyclerAdapter.setData(listOf())

        fView.mailList.layoutManager = LinearLayoutManager(context)
        fView.mailList.adapter = mailRecyclerAdapter
        fView.swipeRefresh.setOnRefreshListener(this)

        if (activity != null){
            (activity as MainActivity).setLoginListnerInterface(this)
        }


        fViewModel.isVisibilityProgressBar.observe(this, progresBarObserver)
//        fViewModel.unreadMailCount.observe(this, unreadMailObserver)
        fViewModel.headerList.observe(this, mailHeaderObserver)
        fViewModel.addHeaderList.observe(this, updateMailHeaderObserver)
        fViewModel.eventId.observe(this, errorObserver)

        return fView
    }

    override fun onResume() {
        super.onResume()
        fViewModel.loadHeadersFromDB()
    }

    override fun onRefresh() {
        fViewModel.loadNewMailHeaders()
    }


    override fun refreshDataAfterLogin() {
        refreshData()
    }

//    private fun setUnreadMail(itemMenuId: Int, count: Int){
//        if(activity != null){
//            if(count != 0){
//                (activity as MainActivity).navigationView.menu.findItem(itemMenuId)
//                    .actionView.unreadMails.text = "$count"
//            }else{
//                (activity as MainActivity).navigationView.menu.findItem(itemMenuId)
//                    .actionView.unreadMails.text = ""
//            }
//        }
//
//    }

    fun refreshData(){
        fViewModel.loadNewMailHeaders()
    }
    inner class RecyclerAdapter :RecyclerView.Adapter<MailHolderInfo>(){

        private var entityList = mutableListOf<MailHeader>()

        fun addData(headerList: List<MailHeader>){
            entityList.addAll(headerList)
            notifyDataSetChanged()
        }

        fun setData(headerList: List<MailHeader>){
            entityList.clear()
            entityList.addAll(headerList)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MailHolderInfo {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mail_info, parent, false)
            return MailHolderInfo(view)
        }

        override fun getItemCount(): Int = entityList.size

        override fun onBindViewHolder(holder: MailHolderInfo, position: Int) {
            holder.itemView.mailAuthor.text = entityList[position].fromName
            holder.itemView.mailSubject.text = entityList[position].subject
            holder.itemView.mailTime.text = entityList[position].timestamp

            when(entityList[position].isRead){
                true -> holder.itemView.setBackgroundResource(R.drawable.item_is_read_background)
                false -> holder.itemView.setBackgroundResource(R.drawable.item_is_not_read_background)
            }

            Picasso.get()
                .load("https://imageserver.eveonline.com/Character/${entityList[position].fromId}_128.jpg")
                .transform(RoundCornerTransform())
                .into(holder.itemView.senderPhoto)
            holder.itemView.setOnClickListener{

                val bundle = Bundle()
                bundle.putLong(MAIL_ID, entityList[position].mailId)
                bundle.putString(FROM, entityList[position].fromName)
                bundle.putString(SUBJECT, entityList[position].subject)
                bundle.putBoolean(IS_READ_MAIL, entityList[position].isRead)

                findNavController().navigate(R.id.readMailFragment, bundle)
            }
        }

        override fun onViewAttachedToWindow(holder: MailHolderInfo) {
            super.onViewAttachedToWindow(holder)
            val position = holder.layoutPosition
            if (position + 2 > entityList.size) {
                fViewModel.loadHeadersAfterId(entityList[position].mailId)
            }
        }
    }

    inner class MailHolderInfo(itemView: View) : RecyclerView.ViewHolder(itemView)
}