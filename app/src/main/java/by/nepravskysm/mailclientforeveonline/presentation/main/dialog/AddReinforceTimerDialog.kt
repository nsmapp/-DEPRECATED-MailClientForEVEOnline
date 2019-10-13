package by.nepravskysm.mailclientforeveonline.presentation.main.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import by.nepravskysm.domain.utils.getEVETime
import by.nepravskysm.domain.utils.stringToLong
import by.nepravskysm.mailclientforeveonline.R
import kotlinx.android.synthetic.main.dialog_reinforce_timer.*
import kotlinx.android.synthetic.main.dialog_reinforce_timer.view.*

class AddReinforceTimerDialog : DialogFragment(){

    private var daysCount: Long = 0
    private var hourCount: Long = 0
    private var minCount: Long = 0
    private var time = ""
    private var reinforceText = ""
    private var reinforceTimerListenear: ReinforceTimerListenear? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val dialog = inflater.inflate(R.layout.dialog_reinforce_timer, container)

        val systemsArray = resources.getStringArray(R.array.array_solar_systems)
        val systemsAdapter = ArrayAdapter(dialog.context, android.R.layout.simple_list_item_1, systemsArray)
        dialog.solarSystem.setAdapter(systemsAdapter)

        dialog.days.doOnTextChanged{text, _, _, _ ->
            daysCount = stringToLong(text.toString())
            setEVETime(dialog)
        }

        dialog.hours.doOnTextChanged{text, _, _, _ ->
            hourCount = stringToLong(text.toString())
            setEVETime(dialog)
        }

        dialog.minuts.doOnTextChanged{text, _, _, _ ->
            minCount = stringToLong(text.toString())
            setEVETime(dialog)
        }

        dialog.cancel.setOnClickListener { dismiss() }
        dialog.add.setOnClickListener {
            createTimer()
            if (reinforceTimerListenear != null){
                reinforceTimerListenear?.addReinforceTimer(createTimer())
            }
            dismiss()
        }
        return dialog
    }

    override fun onResume() {
        super.onResume()
        solarSystem.setText("")
        moreInfo.setText("")
        reinforceTime.text = ""
    }

    fun setReinforceTimerListenear(reinforceTimerListenear: ReinforceTimerListenear){
        this.reinforceTimerListenear = reinforceTimerListenear
    }

    private fun createTimer(): String{

        if(!time.equals("")){
            reinforceText = "\nEVE Time: $time \n"
            if(!solarSystem.text.toString().equals("")){ reinforceText += "Solar system: ${solarSystem.text} \n"}
            if(!moreInfo.text.toString().equals("")){reinforceText+= "Timer info: ${moreInfo.text.toString()} \n" }
            if(hull.isChecked){reinforceText += "Timer type: hull"}
            if(armor.isChecked){reinforceText += "Timer type: armor"}
            if(shield.isChecked){reinforceText += "Timer type: shield"}
        }

        return reinforceText

    }


    private fun setEVETime(view: View){
        time = getEVETime(daysCount, hourCount, minCount)
        view.reinforceTime.text = "EVE time: $time"

    }

    interface ReinforceTimerListenear{
        fun addReinforceTimer(timerInfo: String)
    }


}