package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
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
import kotlinx.android.synthetic.main.item_mail_header.view.*
import kotlinx.android.synthetic.main.stub_mail_subsettings.*

abstract class BaseMailListFragment <VM : BaseMailListViewModel>: Fragment(),
SwipeRefreshLayout.OnRefreshListener,
MainActivity.LoginListener{

    abstract val fViewModel:VM
    val mailRecyclerAdapter = RecyclerAdapter()
    private var isNotVisibleSubMenu = false
    private lateinit var subMenu: View

    private val progresBarObserver = Observer<Boolean>{swipeRefresh.isRefreshing = it}
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
        subMenu = fView.stubSubMenu.inflate()
        subMenu.visibility = View.GONE

        fView.mailList.layoutManager = LinearLayoutManager(context)
        fView.mailList.adapter = mailRecyclerAdapter
        fView.swipeRefresh.setOnRefreshListener(this)

        if (activity != null){
            (activity as MainActivity).setLoginListenerInterface(this)
        }


        fViewModel.isVisibilityProgressBar.observe(viewLifecycleOwner, progresBarObserver)
        fViewModel.headerList.observe(viewLifecycleOwner, mailHeaderObserver)
        fViewModel.addHeaderList.observe(viewLifecycleOwner, updateMailHeaderObserver)
        fViewModel.eventId.observe(viewLifecycleOwner, errorObserver)


        fView.submenuButton.setOnClickListener {
            isNotVisibleSubMenu = if (isNotVisibleSubMenu) {
                fView.submenuButton.setImageResource(R.drawable.ic_arrow_down)
                subMenu.visibility = View.GONE
                markAllAsReadbtn.setOnClickListener(null)
                false

            } else {
                fView.submenuButton.setImageResource(R.drawable.ic_arrow_up)
                subMenu.visibility = View.VISIBLE
                markAllAsReadbtn.setOnClickListener {
                    fViewModel.updateAllMailMetadata()
                }
                true
            }

        }
        fViewModel.loadHeadersFromDB()
        return fView
    }

    override fun onRefresh() {
        fViewModel.loadNewMailHeaders()
    }

    override fun refreshDataAfterLogin() {
        onRefresh()
    }

    fun scrollToTop() {
        mailList.layoutManager?.scrollToPosition(0) ?: Unit
    }

    inner class RecyclerAdapter :RecyclerView.Adapter<MailHolderInfo>(){


        var entityList = mutableListOf<MailHeader>()
        var isNeedScrolTop = false

        fun addData(headerList: List<MailHeader>){
            val newHeaderList = mutableListOf<MailHeader>()
            newHeaderList.addAll(entityList)
            newHeaderList.addAll(headerList)
            setData(newHeaderList)
        }

        fun setData(headerList: List<MailHeader>){
            if (entityList.isNotEmpty() && headerList.isNotEmpty()) {
                isNeedScrolTop = entityList[0].mailId != headerList[0].mailId
            }
            val mailHeaderDiffUtils = MailHeaderDiffUtils(entityList, headerList)
            val diffUtilsResult = DiffUtil.calculateDiff(mailHeaderDiffUtils)
            entityList.clear()
            entityList.addAll(headerList)
            diffUtilsResult.dispatchUpdatesTo(this)

            if (isNeedScrolTop) {
                scrollToTop()
                isNeedScrolTop = false
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MailHolderInfo {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_mail_header, parent, false)
            return MailHolderInfo(view)
        }

        override fun getItemCount(): Int = entityList.size

        override fun onBindViewHolder(holder: MailHolderInfo, position: Int) {
            holder.itemView.mailAuthor.text = entityList[holder.adapterPosition].fromName
            holder.itemView.mailSubject.text = entityList[holder.adapterPosition].subject
            holder.itemView.mailTime.text = entityList[holder.adapterPosition].timestamp
            holder.itemView.mailType.text = entityList[holder.adapterPosition].mailType

            when(entityList[position].isRead){
                true -> holder.itemView.setBackgroundResource(R.drawable.item_is_read_background)
                false -> holder.itemView.setBackgroundResource(R.drawable.item_is_not_read_background)
            }
            Picasso.get()
                .load("https://imageserver.eveonline.com/Character/${entityList[holder.adapterPosition].fromId}_128.jpg")
                .transform(RoundCornerTransform())
                .into(holder.itemView.senderPhoto)
            holder.itemView.setOnClickListener{

                val bundle = Bundle()
                bundle.putLong(MAIL_ID, entityList[holder.adapterPosition].mailId)
                bundle.putString(FROM, entityList[holder.adapterPosition].fromName)
                bundle.putString(SUBJECT, entityList[holder.adapterPosition].subject)
                bundle.putBoolean(IS_READ_MAIL, entityList[holder.adapterPosition].isRead)
                bundle.putString(MAIL_SENT_TIME, entityList[holder.adapterPosition].timestamp)

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

    inner class MailHeaderDiffUtils(
        private val oldList: List<MailHeader>,
        private val newList: List<MailHeader>
    ) : DiffUtil.Callback() {


        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].mailId == newList[newItemPosition].mailId
        }

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].mailId == newList[newItemPosition].mailId
        }

    }


}