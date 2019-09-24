package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.newmail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.nepravskysm.domain.utils.*
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.newmail.dialog.AddNameDialog
import kotlinx.android.synthetic.main.fragment_new_mail.*
import kotlinx.android.synthetic.main.fragment_new_mail.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class NewMailFragment :Fragment(), AddNameDialog.ConfirmNameListener{

    val fViewModel: NewMailViewModel by viewModel()


    var addNameDialog = AddNameDialog()


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
                        fViewModel.subject = "RE: ${arguments?.getString(SUBJECT)!!}"
                        fViewModel.mailBody = "\n \n" +
                                "REPLAY MAIL \n" +
                                "from: ${fViewModel.nameList} \n" +
                                "subject: ${arguments?.getString(SUBJECT)!!} \n \n" +
                                arguments?.getString(MAIL_BODY)!!


                    }
                    FORWARD -> {

                        fViewModel.subject = "FORWARD: ${arguments?.getString(SUBJECT)!!}"
                        fViewModel.mailBody = "\n \n" +
                                "FORWARD MAIL\n" +
                                "from: ${arguments?.getString(FROM)!!} \n" +
                                "subject: ${fViewModel.subject} \n \n"+
                                arguments?.getString(MAIL_BODY)!!
                    }
                }

            }catch (e: Exception){
                fViewModel.nameList.clear()
                fViewModel.subject = ""
                fViewModel.mailBody = ""
                //TODO
            }finally {

                view.toName.setText(fViewModel.nameList.toString())
                view.subject.setText(fViewModel.subject)
                view.mailBody.setText(fViewModel.mailBody)

            }
        }


        addNameDialog.setConfirmNameListener(this)




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


        return view
    }

    override fun confirm(name: String) {
        fViewModel.nameList.add(name)
        toName.setText(fViewModel.nameList.toString())
    }
}