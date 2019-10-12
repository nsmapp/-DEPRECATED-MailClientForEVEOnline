package by.nepravskysm.mailclientforeveonline.presentation.main.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.nepravskysm.domain.entity.Contact
import by.nepravskysm.domain.usecase.character.GetContactUseCase
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.utils.pastImage
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_main.view.characterName
import kotlinx.android.synthetic.main.dialog_add_name.*
import kotlinx.android.synthetic.main.dialog_add_name.view.*
import kotlinx.android.synthetic.main.item_character.view.*
import org.koin.android.ext.android.inject

class AddNameDialog  : DialogFragment(){



    private var confirmNameListener: ConfirmNameListener? = null
    private val getContactUseCase: GetContactUseCase by inject()

    private val recyclerAdapter: CharacterNameAdapter = CharacterNameAdapter()

    fun setConfirmNameListener(confirmNameListener: ConfirmNameListener){
        this.confirmNameListener = confirmNameListener
    }

    override fun onResume() {
        super.onResume()
        name.setText("")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val dialog = inflater.inflate(R.layout.dialog_add_name, container, false)
        getDialog()!!.setTitle("Add the recipient's name")

        dialog.nameRecycler.layoutManager = LinearLayoutManager(dialog.context)
        dialog.nameRecycler.hasFixedSize()
        dialog.nameRecycler.adapter = recyclerAdapter

        getContactUseCase.execute {
            onComplite { recyclerAdapter.setData(it)
            Log.d("logd", "contact count ${it.size}")}

        }



        dialog.add.setOnClickListener {
            if (confirmNameListener != null){
                confirmNameListener!!.confirmName(dialog.name.text.toString())
            }
            dismiss()
        }

        dialog.cancel.setOnClickListener { dismiss() }

        return dialog
    }


    interface ConfirmNameListener{
        fun confirmName(name: String)
    }



    inner class CharacterNameAdapter: RecyclerView.Adapter<CharacterNameAdapter.NameViewHolder>(){

        private var contactList = listOf<Contact>()
        fun setData(contactList: List<Contact>){
            this.contactList = contactList
            notifyDataSetChanged()
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)

            return NameViewHolder(view)
        }

        override fun getItemCount(): Int = contactList.size

        override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
            holder.itemView.characterName.text = contactList[position].contactName
            holder.itemView.setOnClickListener{name.setText(contactList[position].contactName)}
            pastImage(holder.itemView.characterPhoto, contactList[position].contactId)
        }


        inner class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }

}