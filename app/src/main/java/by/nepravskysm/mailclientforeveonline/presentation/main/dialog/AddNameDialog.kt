package by.nepravskysm.mailclientforeveonline.presentation.main.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.nepravskysm.mailclientforeveonline.R
import kotlinx.android.synthetic.main.dialog_add_name.*
import kotlinx.android.synthetic.main.dialog_add_name.view.*

class AddNameDialog  : DialogFragment(){



    private var confirmNameListener: ConfirmNameListener? = null

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