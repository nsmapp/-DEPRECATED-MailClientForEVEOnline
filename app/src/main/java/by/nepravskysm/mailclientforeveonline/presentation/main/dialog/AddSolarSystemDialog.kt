package by.nepravskysm.mailclientforeveonline.presentation.main.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import by.nepravskysm.mailclientforeveonline.R
import kotlinx.android.synthetic.main.dialog_add_solar_system.*
import kotlinx.android.synthetic.main.dialog_add_solar_system.view.*

class AddSolarSystemDialog : DialogFragment(){

    private var addSolarSystemListener: AddSolarSystemListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val dialog = inflater.inflate(R.layout.dialog_add_solar_system, container)
        getDialog()!!.setTitle("Add solar system")
        val systemsArray = resources.getStringArray(R.array.array_solar_systems)
        val systemsAdapter = ArrayAdapter(dialog.context, android.R.layout.simple_list_item_1, systemsArray)
        dialog.solarSystem.setAdapter(systemsAdapter)
        dialog.solarSystem.setText("")
        dialog.add.setOnClickListener {
            if (addSolarSystemListener != null && !dialog.solarSystem.text.toString().equals("")){
                addSolarSystemListener!!.addSolarSystem(dialog.solarSystem.text.toString())
            }
            dismiss() }
        dialog.cancel.setOnClickListener { dismiss() }
        return dialog
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