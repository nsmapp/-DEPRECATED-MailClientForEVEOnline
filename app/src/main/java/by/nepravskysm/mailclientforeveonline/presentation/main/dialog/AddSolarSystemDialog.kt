package by.nepravskysm.mailclientforeveonline.presentation.main.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import by.nepravskysm.mailclientforeveonline.R
import kotlinx.android.synthetic.main.dialog_add_solar_system.*
import kotlinx.android.synthetic.main.dialog_add_solar_system.view.*

class AddSolarSystemDialog : DialogFragment(){

    private var addSolarSystemListener: AddSolarSystemListener? = null

    override fun onStart() {
        super.onStart()
        dialog?.setTitle("Add solar system")
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fDialog = inflater.inflate(R.layout.dialog_add_solar_system, container, false)
        val systemsArray = resources.getStringArray(R.array.array_solar_systems)
        val systemsAdapter =
            ArrayAdapter(fDialog.context, android.R.layout.simple_list_item_1, systemsArray)
        fDialog.solarSystem.setAdapter(systemsAdapter)
        fDialog.solarSystem.setText("")
        fDialog.add.setOnClickListener {
            if (addSolarSystemListener != null && !fDialog.solarSystem.text.toString().equals("")) {
                addSolarSystemListener!!.addSolarSystem(fDialog.solarSystem.text.toString())
            }
            dismiss() }
        fDialog.cancel.setOnClickListener { dismiss() }
        return fDialog
    }

    override fun onResume() {
        super.onResume()
        solarSystem.setText("")
    }

    fun setAddSolarSystemListener(addSolarSystemListener: AddSolarSystemListener){
        this.addSolarSystemListener = addSolarSystemListener
    }

    interface AddSolarSystemListener{
        fun addSolarSystem(solarSystem: String)
    }
}