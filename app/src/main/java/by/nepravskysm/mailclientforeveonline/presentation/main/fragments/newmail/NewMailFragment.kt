package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.newmail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.nepravskysm.domain.utils.MAIL_IS_SENT
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.MainActivity
import by.nepravskysm.mailclientforeveonline.presentation.main.dialog.AddNameDialog
import by.nepravskysm.mailclientforeveonline.presentation.main.dialog.AddReinforceTimerDialog
import by.nepravskysm.mailclientforeveonline.presentation.main.dialog.AddSolarSystemDialog
import by.nepravskysm.mailclientforeveonline.utils.*
import kotlinx.android.synthetic.main.fragment_new_mail.*
import kotlinx.android.synthetic.main.fragment_new_mail.view.*
import kotlinx.android.synthetic.main.stub_reply_forward_mail.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class NewMailFragment : Fragment(),
    AddNameDialog.ConfirmNameListener,
    AddReinforceTimerDialog.ReinforceTimerListenear,
    AddSolarSystemDialog.AddSolarSystemListener {

    val fViewModel: NewMailViewModel by viewModel()

    private var isNotReplyMailVisible = true
    private val addNameDialog = AddNameDialog()
    private val addSolarSystemDialog = AddSolarSystemDialog()
    private val reinforceDialog = AddReinforceTimerDialog()

    private val progresBarObserver = Observer<Boolean> {
        val imm =
            (activity as MainActivity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        Objects.requireNonNull(imm).hideSoftInputFromWindow(view?.windowToken, 0)

        if (it) {
            showProgresBar()
        } else {
            hideProgresBar()
        }
    }

    private val eventIdObserver = Observer<Long> { eventId ->
        makeToastMessage((activity as MainActivity), eventId)
        if (eventId == MAIL_IS_SENT) {
            findNavController().navigate(R.id.allMailsFragment)
        }
    }

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
                        fViewModel.initReplyMail(
                            arguments?.getString(SUBJECT)!!,
                            arguments?.getString(FROM)!!,
                            arguments?.getString(REPLY_MAIL_BODY)!!,
                            arguments?.getString(MAIL_SENT_TIME)!!
                        )
                        initStubView(view)
                    }
                    FORWARD -> {
                        fViewModel.initForwardMail(
                            arguments?.getString(SUBJECT)!!,
                            arguments?.getString(FROM)!!,
                            arguments?.getString(REPLY_MAIL_BODY)!!,
                            arguments?.getString(MAIL_SENT_TIME)!!
                        )
                        initStubView(view)
                    }
                    FEEDBACK -> {
                        fViewModel.nameList.add(arguments?.getString(FROM)!!)
                        fViewModel.subject = arguments?.getString(SUBJECT)!!

                    }
                }


            }catch (e: Exception){
                fViewModel.nameList.clear()
                fViewModel.subject = ""
                fViewModel.mailBody = ""
            }finally {

                view.toName.setText(fViewModel.nameList.toString())
                view.subject.setText(fViewModel.subject)
            }
        }

        initOnClickListener(view)

        addNameDialog.setConfirmNameListener(this)
        addSolarSystemDialog.setAddSolarSystemListener(this)
        reinforceDialog.setReinforceTimerListenear(this)

        view.mailBody.doOnTextChanged{text, _, _, _ ->
            view.mailLendth.text = "${text!!.length}/7000"
            fViewModel.mailBody = text.toString()
        }

        fViewModel.isVisibilityProgressBar.observe(viewLifecycleOwner, progresBarObserver)
        fViewModel.eventId.observe(viewLifecycleOwner, eventIdObserver)

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

    private fun initOnClickListener(rootView: View) {
        rootView.reinforceTimer.setOnClickListener {
            reinforceDialog.show(activity!!.supportFragmentManager, "reinforce")
        }
        rootView.toName.setOnClickListener {
            addNameDialog.show(activity!!.supportFragmentManager, "addName")
        }
        rootView.clear.setOnClickListener {
            rootView.toName.setText("")
            fViewModel.nameList.clear()
        }
        rootView.sendMailBtn.setOnClickListener {
            fViewModel.subject = rootView.subject.text.toString()
            fViewModel.sendMail()
        }

        rootView.solarSystemDialog.setOnClickListener {
            addSolarSystemDialog.show(activity!!.supportFragmentManager, "addsolarsystem")
        }

    }

    private fun initStubView(rootView: View) {
        rootView.stubReplayForward.inflate()
        rootView.replyMail.visibility = View.GONE
        rootView.collapseBtn.setOnClickListener {
            if (isNotReplyMailVisible) {
                rootView.collapseBtn.setImageResource(R.drawable.ic_arrow_down)
                rootView.replyMail.visibility = View.VISIBLE
                isNotReplyMailVisible = false
            } else {
                rootView.collapseBtn.setImageResource(R.drawable.ic_arrow_up)
                rootView.replyMail.visibility = View.GONE
                isNotReplyMailVisible = true
            }
        }
        pastHtmlTextToMailBody(rootView.replyMail, fViewModel.replyMail)
        rootView.replyMail.doOnTextChanged { text, _, _, _ ->
            fViewModel.replyMail = text.toString()
        }
    }

    private fun showProgresBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgresBar() {
        progressBar.visibility = View.GONE
    }

}