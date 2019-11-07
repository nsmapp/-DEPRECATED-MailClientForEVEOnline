package by.nepravskysm.mailclientforeveonline.presentation.main.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import by.nepravskysm.domain.usecase.character.GetContactFromDBUseCase
import by.nepravskysm.mailclientforeveonline.R
import kotlinx.android.synthetic.main.dialog_add_name.*
import kotlinx.android.synthetic.main.dialog_add_name.view.*
import org.koin.android.ext.android.inject

class AddNameDialog  : DialogFragment(){

    private var confirmNameListener: ConfirmNameListener? = null
    private val getContactFromDBUseCase: GetContactFromDBUseCase by inject()

    fun setConfirmNameListener(confirmNameListener: ConfirmNameListener){
        this.confirmNameListener = confirmNameListener
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

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

        getContactFromDBUseCase.execute {
            onComplite {
                val nameArray = it.map { it.contactName }.toTypedArray()
                val nameAdapter = ArrayAdapter(dialog.context, android.R.layout.simple_list_item_1, nameArray)
                dialog.name.setAdapter(nameAdapter)
            }
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
}