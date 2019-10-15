package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.newmail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.MainActivity
import by.nepravskysm.mailclientforeveonline.presentation.main.dialog.AddNameDialog
import by.nepravskysm.mailclientforeveonline.presentation.main.dialog.AddSolarSystemDialog
import by.nepravskysm.mailclientforeveonline.presentation.main.dialog.AddReinforceTimerDialog
import by.nepravskysm.mailclientforeveonline.utils.*
import kotlinx.android.synthetic.main.fragment_new_mail.*
import kotlinx.android.synthetic.main.fragment_new_mail.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class NewMailFragment :Fragment(), AddNameDialog.ConfirmNameListener,
    AddReinforceTimerDialog.ReinforceTimerListenear,
    AddSolarSystemDialog.AddSolarSystemListener{

    val fViewModel: NewMailViewModel by viewModel()
    private val addNameDialog = AddNameDialog()
    private val addSolarSystemDialog = AddSolarSystemDialog()
    private val reinforceDialog = AddReinforceTimerDialog()

    private val errorIdObserver= Observer<Long>{ errorId -> showErrorToast((activity as MainActivity), errorId)}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_new_mail, container, false)
        view.toName.keyListener = null
        if(arguments?.get(BUNDLE_TYPE) != null){

            try {
                when(arguments?.getString((BUNDLE_TYPE))){

                    REPLAY -> {
                        fViewModel.nameList.add(arguments?.getString(FROM)!!)
                        fViewModel.initReplayMail(arguments?.getString(SUBJECT)!!,
                            arguments?.getString(FROM)!!,
                            arguments?.getString(MAIL_BODY)!!)
                    }
                    FORWARD -> {
                        fViewModel.initForwardMail(arguments?.getString(SUBJECT)!!,
                            arguments?.getString(FROM)!!,
                            arguments?.getString(MAIL_BODY)!!)
                    }
                }

            }catch (e: Exception){
                fViewModel.nameList.clear()
                fViewModel.subject = ""
                fViewModel.mailBody = ""
            }finally {

                view.toName.setText(fViewModel.nameList.toString())
                view.subject.setText(fViewModel.subject)
                pastHtmlTextToMailBody(view.mailBody, fViewModel.mailBody)

            }

            fViewModel.errorId.observe(this, errorIdObserver)
        }


        addNameDialog.setConfirmNameListener(this)
        addSolarSystemDialog.setAddSolarSystemListener(this)
        reinforceDialog.setReinforceTimerListenear(this)

        view.reinforceTimer.setOnClickListener {
            reinforceDialog.show(activity!!.supportFragmentManager, "reinforce")
        }
        view.toName.setOnClickListener {
            addNameDialog.show(activity!!.supportFragmentManager, "addName")
        }
        view.clear.setOnClickListener {
            view.toName.setText("")
            fViewModel.nameList.clear()
        }
        view.sendMailBtn.setOnClickListener {
            fViewModel.sendMail()
        }
        view.mailBody.doOnTextChanged{text, _, _, _ ->
            view.mailLendth.text = "${text!!.length}/8000"
            fViewModel.mailBody = text.toString()
        }
        view.solarSystemDialog.setOnClickListener {
            addSolarSystemDialog.show(activity!!.supportFragmentManager, "addsolarsystem")
        }

        return view
    }

    override fun addReinforceTimer(timerInfo: String){
        val text = mailBody.text.toString() + timerInfo
        mailBody.setText(text)
    }

    override fun confirmName(name: String) {
        fViewModel.nameList.add(name)
        toName.setText(fViewModel.nameList.toString())
    }

    override fun addSolarSystem(solarSystem: String) {
        val text = mailBody.text.toString() +" $solarSystem"
        mailBody.setText(text)
    }

}