package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.recycler

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.mailclientforeveonline.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_mail_info.view.*



class MailRecyclerAdapter : RecyclerView.Adapter<MailRecyclerAdapter.MailInfoHolder>(){



    private var mItemClickListener: OnItemClickListener? = null
    private var entityList: List<MailHeader> = mutableListOf()


    fun setItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mItemClickListener = mItemClickListener
    }

    fun setEntiies(entities: List<MailHeader>){
        entityList = entities
        Log.d("logdee", "entitiesSize ${entityList.size}")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MailInfoHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mail_info, parent, false)

        return MailInfoHolder(view)
    }

    override fun getItemCount(): Int {
        return entityList.size
    }

    override fun onBindViewHolder(holder: MailInfoHolder, position: Int) {

        holder.itemView.mailAuthor.text = entityList[position].fromName
        holder.itemView.mailSubject.text = entityList[position].subject
        holder.itemView.mailTime.text = entityList[position].timestamp

        when(entityList[position].isRead){
            true -> holder.itemView.setBackgroundColor(Color.GRAY)
            false -> holder.itemView.setBackgroundColor(Color.CYAN)

        }

        Picasso.get()
            .load("https://imageserver.eveonline.com/Character/${entityList[position].from}_128.jpg")
            .into(holder.itemView.senderPhoto)
        holder.itemView.setOnClickListener{

            if (mItemClickListener != null) {
                mItemClickListener?.onItemClick(
                    entityList[position].mailId,
                    entityList[position].fromName,
                    entityList[position].subject,
                    entityList[position].isRead)
            }
        }

    }

    class MailInfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    interface OnItemClickListener {
        fun onItemClick(mailId: Long,
                        fromName: String,
                        subject: String,
                        isRead: Boolean)
    }
}